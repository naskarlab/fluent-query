package com.naskar.fluentquery.impl;

public interface DeleteConverter<E> {

	<T> E convert(DeleteImpl<T> deleteImpl);

}
