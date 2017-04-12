package boxalino.client.SDK;

@FunctionalInterface
public interface CheckedFunction<T, R> {
	R apply(T t) throws Exception;
}
