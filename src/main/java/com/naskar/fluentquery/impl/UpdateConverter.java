package com.naskar.fluentquery.impl;

public interface UpdateConverter<E> {

	<T> E convert(UpdateImpl<T> updateImpl);

}
