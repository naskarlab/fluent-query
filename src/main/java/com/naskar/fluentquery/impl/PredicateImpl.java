package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.naskar.fluentquery.Join;
import com.naskar.fluentquery.Predicate;

public class PredicateImpl<T, R, I, B> implements Predicate<T, R, B> {
	
	public enum Type { SPEC_AND, SPEC_OR, AND, OR };
	
	private B impl;
	private Function<T, R> property;
	private Type type;
	private List<Consumer<Predicate<T, R, I>>> actions;
	
	public PredicateImpl(B impl, Function<T, R> property, Type type) {
		this.impl = impl;
		this.property = property;
		this.type = type;
		this.actions = new ArrayList<Consumer<Predicate<T, R, I>>>();
	}
	
	public Function<T, R> getProperty() {
		return property;
	}
	
	public Type getType() {
		return type;
	}
	
	public List<Consumer<Predicate<T, R, I>>> getActions() {
		return actions;
	}

	@Override
	public B eq(final R value) {
		actions.add(i -> i.eq(value));
		return impl;
	}
	
	@Override
	public B ne(R value) {
		actions.add(i -> i.ne(value));
		return impl;
	}
	
	@Override
	public B gt(R value) {
		actions.add(i -> i.gt(value));
		return impl;
	}
	
	@Override
	public B ge(R value) {
		actions.add(i -> i.ge(value));
		return impl;
	}
	
	@Override
	public B lt(R value) {
		actions.add(i -> i.lt(value));
		return impl;
	}
	
	@Override
	public B le(R value) {
		actions.add(i -> i.le(value));
		return impl;
	}
	
	@Override
	public B like(R value) {
		actions.add(i -> i.like(value));
		return impl;
	}
	
	@Override
	public B isNull() {
		actions.add(i -> i.isNull());
		return impl;
	}
	
	@Override
	public B isNotNull() {
		actions.add(i -> i.isNotNull());
		return impl;
	}
	
	@Override
	public <J> B in(Class<J> clazz, Join<J, T> action) {
		actions.add(i -> i.in(clazz, action));
		return impl;
	}
	
	@Override
	public <J> B notIn(Class<J> clazz, Join<J, T> action) {
		actions.add(i -> i.notIn(clazz, action));
		return impl;
	}

}
