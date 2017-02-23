package com.boxalino.p13n.pool;


@FunctionalInterface
public interface CheckedSupplier<R> {
	R get() throws Exception;
}
