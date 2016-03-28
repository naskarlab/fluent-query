package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;

public class PredicateImpl<T, R> implements Predicate<T, R> {
	
	public enum Type { AND, OR };
	
	private QueryImpl<T> queryImpl;
	private Function<T, R> property;
	private Type type;
	private List<Consumer<Predicate<T, R>>> actions;
	
	public PredicateImpl(QueryImpl<T> queryImpl, Function<T, R> property, Type type) {
		this.queryImpl = queryImpl;
		this.property = property;
		this.type = type;
		this.actions = new ArrayList<Consumer<Predicate<T, R>>>();
	}
	
	public Function<T, R> getProperty() {
		return property;
	}
	
	public Type getType() {
		return type;
	}
	
	public List<Consumer<Predicate<T, R>>> getActions() {
		return actions;
	}

	@Override
	public Query<T> eq(final R value) {
		actions.add(i -> i.eq(value));
		return queryImpl;
	}

	@Override
	public Query<T> like(R value) {
		actions.add(i -> i.like(value));
		return queryImpl;
	}
	
	@Override
	public Query<T> gt(R value) {
		actions.add(i -> i.gt(value));
		return queryImpl;
	}

}
