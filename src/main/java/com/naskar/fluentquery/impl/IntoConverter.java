package com.naskar.fluentquery.impl;

public interface IntoConverter<E> {

	<T> E convert(IntoImpl<T> intoImpl);

}
