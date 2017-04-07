package boxalino.p13n.pool;

import org.apache.thrift.transport.THttpClient;

import com.boxalino.p13n.api.thrift.P13nService;

public class ClientWithTHttpClient extends CloseableClient {

	private final THttpClient tHttpClient;
	
	public ClientWithTHttpClient(THttpClient tHttpClient, P13nService.Iface client) {
		super(client);
		this.tHttpClient = tHttpClient;
	}
	
	@Override
	protected void close() {
		tHttpClient.close();
	}

}
