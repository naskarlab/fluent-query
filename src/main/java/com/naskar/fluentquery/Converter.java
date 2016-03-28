package com.naskar.fluentquery;

import com.naskar.fluentquery.impl.QueryImpl;

public interface Converter<E> {

	<T> E convert(QueryImpl<T> queryImpl);

}
