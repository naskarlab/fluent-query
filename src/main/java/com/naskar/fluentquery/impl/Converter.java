package com.naskar.fluentquery.impl;

public interface Converter<E> {

	<T> E convert(QueryImpl<T> queryImpl);

}
