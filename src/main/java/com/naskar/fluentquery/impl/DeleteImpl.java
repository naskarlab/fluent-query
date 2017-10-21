package com.naskar.fluentquery.impl;

import com.naskar.fluentquery.Delete;
import com.naskar.fluentquery.converters.PredicateProvider;

public class DeleteImpl<T>
	extends WhereImpl<T, Delete<T>, DeleteImpl<T>> 
	implements Delete<T>, PredicateProvider<T, DeleteImpl<T>> {
	
	private Class<T> clazz;

	public DeleteImpl(Class<T> clazz) {
		super(clazz);
		this.clazz = clazz;
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	@Override
	public <E> E to(DeleteConverter<E> converter) {
		return converter.convert(this);
	}
	
}
