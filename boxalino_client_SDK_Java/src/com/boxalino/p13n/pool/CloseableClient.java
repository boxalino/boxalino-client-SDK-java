package com.boxalino.p13n.pool;



import com.boxalino.p13n.api.thrift.P13nService;


public abstract class CloseableClient {
	
	private final P13nService.Iface client;
	
	public CloseableClient(P13nService.Iface client) {
		this.client = client;
	}

	public P13nService.Iface getClient() {
		return client;
	}
	
	protected abstract void close();
	
}