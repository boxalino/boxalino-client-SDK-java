package com.boxalino.p13n.pool;


@FunctionalInterface
public interface CheckedFunction<T, R> {
	R apply(T t) throws Exception;
}
