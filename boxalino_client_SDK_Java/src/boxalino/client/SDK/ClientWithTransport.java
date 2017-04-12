package boxalino.client.SDK;

import org.apache.thrift.transport.TTransport;

import com.boxalino.p13n.api.thrift.P13nService.Client;

class ClientWithTransport extends CloseableClient {

	private final TTransport transport;

	public ClientWithTransport(TTransport transport, Client client) {
		super(client);
		this.transport = transport;
	}

	@Override
	protected void close() {
		transport.close();
	}

}
