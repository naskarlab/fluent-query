package com.naskar.fluentquery.impl;

public interface QueryConverter<E> {

	<T> E convert(QueryImpl<T> queryImpl);

}
