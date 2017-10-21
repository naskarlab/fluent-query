package com.naskar.fluentquery.impl;

public class AliasOrderByImpl<T> implements OrderByImpl<T> {
	
	private T target;
	private String alias;
	private OrderByImpl.OrderByType type;
	
	public AliasOrderByImpl(T target, String alias) {
		this.target = target;
		this.alias = alias;
		this.type = OrderByType.ASC;
	}
	
	public String getAlias() {
		return alias;
	}
	
	@Override
	public OrderByType getType() {
		return type;
	}
	
	@Override
	public T asc() {
		this.type = OrderByType.ASC; 
		return target;
	}
	
	@Override
	public T desc() {
		this.type = OrderByType.DESC;
		return target;
	}

}
