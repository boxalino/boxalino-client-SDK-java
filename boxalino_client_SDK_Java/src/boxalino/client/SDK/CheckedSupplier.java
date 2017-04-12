package boxalino.client.SDK;

@FunctionalInterface
public interface CheckedSupplier<R> {
	R get() throws Exception;
}
