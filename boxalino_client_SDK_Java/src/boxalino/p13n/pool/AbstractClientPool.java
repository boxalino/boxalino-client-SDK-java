package boxalino.p13n.pool;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.boxalino.p13n.api.thrift.P13nService;
import com.boxalino.p13n.api.thrift.P13nService.Client;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;

public abstract class AbstractClientPool implements ClientPool {

	private final String host;
	
	private final int port;
	
	private final String url, user, pwd;
	
	private final AtomicInteger openClients = new AtomicInteger();
	
	public AbstractClientPool(String host, int port) {
		this.host = host;
		this.port = port;
		this.url = null;
		this.user = null;
		this.pwd = null;
	}
	
	public AbstractClientPool(String url, String user, String pwd) {
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.host = null;
		this.port = -1;
	}
	
	final protected CloseableClient createClient() {
		openClients.incrementAndGet();
		if (isHttp()) {
			return createClientWithTHttpClient();
		} else {
			return createClientWithTransport();
		}
	}
	
	private ClientWithTHttpClient createClientWithTHttpClient() {
		HttpClientBuilder builder = HttpClientBuilder.create();
                builder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, false));
		if (user != null) {
                    
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pwd));
			builder.setDefaultCredentialsProvider(credentialsProvider);
                       
		}
		HttpDecorator httpDecorator = new HttpDecorator();
		builder.addInterceptorLast(httpDecorator);
		CloseableHttpClient httpClient = builder.build();
		try {
			THttpClient tHttpClient = new THttpClient(url, httpClient);
			TProtocol tProtocol = new TCompactProtocol(tHttpClient);
			P13nService.Iface client = DecoratorProxy.of(new Client(tProtocol), httpDecorator);
			return new ClientWithTHttpClient(tHttpClient, client);
		} catch (TTransportException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("resource")
	private CloseableClient createClientWithTransport() {
		TTransport transport = new TSocket(host, port);
		try {
			transport.open();
		} catch (TTransportException e) {
			throw new RuntimeException(e);
		}
		P13nService.Client client = new P13nService.Client(new TBinaryProtocol(transport));
		return new ClientWithTransport(transport, client);
	}
	
	private boolean isHttp() {
		return url != null;
	}
	
	final protected void destroyClient(CloseableClient cc) {
		try {
			openClients.decrementAndGet();
			cc.close();
		} catch (Exception ignore) {}
	}
	
	@Override
	public CloseableClient borrowClient() {
		int maxTries = 3;
		Exception lastCause = null;
		for (int i = 0; i < maxTries; i++) {
			try {
				return poolBorrowObject();
			} catch (Exception e) {
				lastCause = e;
			}
		}
		throw new RuntimeException("failed to acquire pooled object within " + maxTries + " tries", lastCause);
	}
	
	protected abstract CloseableClient poolBorrowObject();
	
	@Override
	public void tryRecover(Throwable t) {
		if (!isRecoverable(t)) throw new RuntimeException(t);
		
		ensureRefreshed();
	}
	
	private boolean isRecoverable(Throwable t) {
		return t instanceof TTransportException || t.getCause() instanceof TTransportException;
	}

	/**
	 * must either refresh the pool and / or block until the pool is refreshed, the implementation
	 * must account for concurrent calls under load
	 */
	protected abstract void ensureRefreshed();
	
	protected abstract void close();

	@Override
	public abstract void returnClient(CloseableClient tc);
	
	public int openClients() {
		return openClients.get();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[openClients=" + openClients() + "]";
	}
	
}
