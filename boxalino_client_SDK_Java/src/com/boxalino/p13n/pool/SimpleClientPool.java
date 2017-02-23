package com.boxalino.p13n.pool;



import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class SimpleClientPool extends AbstractClientPool {
	
	private static final int MAX_REFRESH_FREQUENCY_MS = 1000;
	
	private final Lock poolLock = new ReentrantLock();
	
	private long refreshCount;
	
	private long lastRefresh;
	
	private final int minIdle, maxIdle;
	
	private volatile GenericObjectPool<CloseableClient> pool;
	
	public SimpleClientPool(String host, int port) {
		this(host, port, 4, 16);
	}
	
	public SimpleClientPool(String url, String user, String pwd) {
		this(url, user, pwd, 4, 16);
	}
	
	public SimpleClientPool(String host, int port, int minIdle, int maxIdle) {
		super(host, port);
		this.minIdle = minIdle;
		this.maxIdle = maxIdle;
		pool = mkPool();
	}
	
	public SimpleClientPool(String url, String user, String pwd, int minIdle, int maxIdle) {
		super(url, user, pwd);
		this.minIdle = minIdle;
		this.maxIdle = maxIdle;
		pool = mkPool();
	}

	private GenericObjectPool<CloseableClient> mkPool() {
		final Lock lock = this.poolLock;
		lock.lock();
		try {
			BasePooledObjectFactory<CloseableClient> factory = new BasePooledObjectFactory<CloseableClient>() {
				
				@Override
				public CloseableClient create() throws Exception {
					return createClient();
				}
				
				@Override
				public void destroyObject(PooledObject<CloseableClient> p) throws Exception {
					destroyClient(p.getObject());
				}
	
				@Override
				public PooledObject<CloseableClient> wrap(CloseableClient tc) {
					return new DefaultPooledObject<CloseableClient>(tc);
				}
				
			};
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setBlockWhenExhausted(false);
			config.setMinIdle(minIdle);
			config.setMaxIdle(maxIdle);
			config.setMaxTotal(-1);
			GenericObjectPool<CloseableClient> newPool = new GenericObjectPool<CloseableClient>(factory, config);
			try {
				newPool.preparePool();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return newPool;
		} finally {
			lock.unlock();
		}
	}
	
	private void refreshPool() {
		final Lock lock = poolLock;
		lock.lock();
		try {
			GenericObjectPool<CloseableClient> oldPool = pool;
			pool = mkPool();
			if (oldPool != null) {
				oldPool.close();
				oldPool.clear();
			}
			refreshCount++;
			lastRefresh = System.currentTimeMillis();
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	protected void ensureRefreshed() {
		final Lock lock = poolLock;
		if (!lock.tryLock()) {
			awaitRefreshed();
			return;
		}
		try {
			if (!didJustRefresh()) {
				refreshPool();
			}
		} finally {
			lock.unlock();
		}
	}
	
	private void awaitRefreshed() {
		final Lock lock = poolLock;
		lock.lock();
		lock.unlock();
	}

	@Override
	protected CloseableClient poolBorrowObject() {
		int maxTries = 3;
		Exception lastCause = null;
		for (int i = 0; i < maxTries; i++) {
			try {
				return pool.borrowObject();
			} catch (Exception e) {
				lastCause = e;
			}
		}
		throw new RuntimeException("failed to acquire pooled object within " + maxTries + " tries", lastCause);
	}

	@Override
	public void returnClient(CloseableClient tc) {
		try {
			pool.returnObject(tc);
		} catch (IllegalStateException e) {
			// common case when refreshing pool
			if (!didJustRefresh()) throw e;
		}
	}
	
	private boolean didJustRefresh() {
		if (refreshCount <= 0) return false;
		
		return System.currentTimeMillis() - lastRefresh < MAX_REFRESH_FREQUENCY_MS;
	}

}
