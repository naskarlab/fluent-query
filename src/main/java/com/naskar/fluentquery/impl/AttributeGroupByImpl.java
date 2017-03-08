package com.naskar.fluentquery.impl;

import java.util.function.Function;

public class AttributeGroupByImpl<T, R> implements GroupByImpl {
	
	private Function<T, R> property;
	
	public AttributeGroupByImpl(Function<T, R> property) {
		this.property = property;
	}
	
	public Function<T, R> getProperty() {
		return property;
	}
	
}
