package boxalino.client.SDK;

import com.boxalino.p13n.api.thrift.P13nService;

public interface ClientPool {

	CloseableClient borrowClient();
	
	/**
	 * ensures that the pool is refreshed, if the specified Throwable can be recovered from.
	 * re-throws as RuntimeException otherwise
	 */
	void tryRecover(Throwable t) throws RuntimeException;
	
	void returnClient(CloseableClient tc);
	
	default <T> T withClient(CheckedFunction<P13nService.Iface, T> fn) {
		return withClient(fn, 3);
	}
	
	/**
	 * tries up to {@code maxTries} times to run {@code fn} with a Client, only retries if an exception
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
				if (--maxTries < 0) throw new RuntimeException("max tries exceeded", e);
				
				tryRecover(e);
			} finally {
				returnClient(cc);
			}
		}
	}
	
}
