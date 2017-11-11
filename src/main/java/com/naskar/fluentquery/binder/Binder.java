package com.naskar.fluentquery.binder;

public interface Binder<T, R> {

	R bind(T t);

}
