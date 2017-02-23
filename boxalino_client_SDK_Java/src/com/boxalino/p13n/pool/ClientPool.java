package com.boxalino.p13n.pool;


import com.boxalino.p13n.api.thrift.P13nService;

public interface ClientPool {

	CloseableClient borrowClient();
	
	RuntimeException convertException(Throwable t);
	
	void returnClient(CloseableClient tc);
	
	default <T> T withClient(CheckedFunction<P13nService.Iface, T> fn) {
		return withClient(fn, 3);
	}
	
	/**
	 * tries up to {@code maxTries} times to run fn with a Client, only retries if an exception
	 * is thrown indicating that the connection failed. Note that, therefore part of fn may run multiple times
	 * for one {@code withClient} call.
	 * 
	 * @return the result of fn
	 */
	default <T> T withClient(CheckedFunction<P13nService.Iface, T> fn, int maxTries) {
		for (;;) {
			CloseableClient cc = borrowClient();
			try {
				return fn.apply(cc.getClient());
			} catch (Exception e) {
				RuntimeException converted = convertException(e);
				if (converted != null) throw converted;
				
				if (--maxTries < 0) throw new RuntimeException("max tries exceeded");
			} finally {
				returnClient(cc);
			}
		}
	}
	
}
