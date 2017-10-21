package com.naskar.fluentquery.impl;

import java.util.function.Function;

public class AttributeOrderByImpl<I, T, R> implements OrderByImpl<I> {
	
	private I target;
	private Function<T, R> property;
	private OrderByImpl.OrderByType type;
	
	public AttributeOrderByImpl(I target, Function<T, R> property) {
		this.target = target;
		this.property = property;
		this.type = OrderByType.ASC;
	}
	
	public Function<T, R> getProperty() {
		return property;
	}
	
	@Override
	public OrderByType getType() {
		return type;
	}
	
	@Override
	public I asc() {
		this.type = OrderByType.ASC; 
		return target;
	}
	
	@Override
	public I desc() {
		this.type = OrderByType.DESC;
		return target;
	}

}
