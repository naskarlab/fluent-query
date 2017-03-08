package com.naskar.fluentquery.impl;

import java.util.function.Function;

import com.naskar.fluentquery.OrderBy;
import com.naskar.fluentquery.Select;

public class SelectImpl<T> implements Select {
	
	private QueryImpl<T> queryImpl;
	private Function<T, ?> property;
	
	private Function<String, String> action;
	private String alias;
	
	public <R> SelectImpl(QueryImpl<T> queryImpl, Function<T, R> property) {
		this.queryImpl = queryImpl;
		this.property = property;
	}

	public Function<String, String> getAction() {
		return action;
	}
	
	public String getAlias() {
		return alias;
	}
	
	@Override
	public Select func(Function<String, String> action, String alias) {
		this.action = action;
		this.alias = alias;
		return this;
	}
	
	@Override
	public Select groupBy() {
		if(this.alias != null) {
			queryImpl.groupBy(this.alias);
		} else {
			queryImpl.groupBy(property);
		}
		return this;
	}
	
	@Override
	public OrderBy<Select> orderBy() {
		if(this.alias != null) {
			return queryImpl.orderBy(this, this.alias);
		} else {
			return queryImpl.orderBy(this, property);
		}
	}
	
}
