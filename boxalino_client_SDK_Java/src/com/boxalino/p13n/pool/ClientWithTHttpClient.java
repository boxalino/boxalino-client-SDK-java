package com.boxalino.p13n.pool;


import org.apache.thrift.transport.THttpClient;

import com.boxalino.p13n.api.thrift.P13nService;

public class ClientWithTHttpClient extends CloseableClient {

	private final THttpClient tHttpClient;
	
	private final HttpDecorator profileIdDecorator;
	
	public ClientWithTHttpClient(THttpClient tHttpClient, P13nService.Iface client, HttpDecorator profileIdDecorator) {
		super(client);
		this.tHttpClient = tHttpClient;
		this.profileIdDecorator = profileIdDecorator;
	}

	public void notifyProfileId(String profileId) {
		profileIdDecorator.setNextProfileId(profileId);
	}
	
	@Override
	protected void close() {
		tHttpClient.close();
	}

}
