package com.naskar.fluentquery.impl;

import java.util.function.Function;

import com.naskar.fluentquery.OrderBy;
import com.naskar.fluentquery.Query;

public class OrderByImpl<T, R> implements OrderBy<T> {
	
	public enum OrderByType { ASC, DESC }
	
	private QueryImpl<T> queryImpl;
	private Function<T, R> property;
	private OrderByType type;
	
	public OrderByImpl(QueryImpl<T> queryImpl, Function<T, R> property) {
		this.queryImpl = queryImpl;
		this.property = property;
		this.type = OrderByType.ASC;
	}
	
	public Function<T, R> getProperty() {
		return property;
	}
	
	public OrderByType getType() {
		return type;
	}
	
	@Override
	public Query<T> asc() {
		this.type = OrderByType.ASC; 
		return queryImpl;
	}
	
	@Override
	public Query<T> desc() {
		this.type = OrderByType.DESC;
		return queryImpl;
	}

}
