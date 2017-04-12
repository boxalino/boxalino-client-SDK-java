package boxalino.client.SDK;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PoolProvider {

	private static final PoolProvider DEFAULT = newProvider();
	
	private final Lock lock = new ReentrantLock();
	
	private volatile String url, username, pwd;
	
	private volatile SimpleClientPool pool;
	
	public static ClientPool getDefault(String url, String username, String pwd) {
		return DEFAULT.get(url, username, pwd);
	}
	
	public static ClientPool getDefault() {
		return DEFAULT.get();
	}
	
	public ClientPool get(String url, String username, String pwd) {
		if (checkState(url, username, pwd)) return pool;
		
		final Lock lock = this.lock;
		lock.lock();
		try {
			if (checkState(url, username, pwd)) return pool;
			
			SimpleClientPool oldPool = pool;
			pool = new SimpleClientPool(url, username, pwd);
			this.url = url;
			this.username = username;
			this.pwd = pwd;
			if (oldPool != null) oldPool.close();
			
			return pool;
		} finally {
			lock.unlock();
		}
	}
	
	public ClientPool get() {
		ClientPool pool = this.pool;
		if (pool == null) throw new IllegalStateException("pool has not been initialized");
		
		return pool;
	}
	
	private boolean checkState(String url, String username, String pwd) {
		if (pool == null) return false;
		
		return eq(this.url, url) && eq(this.username, username) && eq(this.pwd, pwd);
	}
	
	private boolean eq(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}
	
	/**
	 * must only be used if multiple concurrent pools are required due to multiple url's / credentials
	 */
	public static PoolProvider newProvider() {
		return new PoolProvider();
	}
	
	private PoolProvider() {}
	
}
