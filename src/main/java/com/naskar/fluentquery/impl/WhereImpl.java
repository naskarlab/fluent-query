	package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.Whereable;
import com.naskar.fluentquery.converters.PredicateProvider;
import com.naskar.fluentquery.impl.PredicateImpl.Type;

public class WhereImpl<T, I, B> implements Whereable<T, I>, PredicateProvider<T, B> {

	private Class<T> clazz;
	private List<PredicateImpl<T, Object, I, B>> predicates;

	public WhereImpl(Class<T> clazz) {
		this.clazz = clazz;
		this.predicates = new ArrayList<PredicateImpl<T, Object, I, B>>();
	}
	
	@SuppressWarnings("unchecked")
	public List<PredicateImpl<T, Object, I, B>> getPredicates() {
		return predicates;
	}
	
	@Override
	public <R> Predicate<T, R, I> where(Function<T, R> property) {
		return and(property);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public I whereSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_AND);
		return (I)this;
	}
	
	@SuppressWarnings("unchecked")
	private void addSpec(Consumer<Query<T>> query, Type type) {
		PredicateImpl<T, Object, I, B> p = new PredicateImpl<T, Object, I, B>((B) this, i -> {
			
			QueryImpl<T> queryImpl = new QueryImpl<T>(clazz);
			query.accept(queryImpl);
			return queryImpl;
			
		}, type);
		
		predicates.add((PredicateImpl<T, Object, I, B>) p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R, I> and(Function<T, R> property) {
		PredicateImpl<T, R, I, B> p = new PredicateImpl<T, R, I, B>((B) this, property, Type.AND);
		predicates.add((PredicateImpl<T, Object, I, B>) p);
		return (Predicate<T, R, I>)(Object)p;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R, I> andIf(Supplier<Boolean> callIf, Function<T, R> property) {
		PredicateImpl<T, R, I, B> p = new PredicateImpl<T, R, I, B>((B) this, property, Type.AND);
		if(callIf.get()) {
			predicates.add((PredicateImpl<T, Object, I, B>)p);
		}
		return (Predicate<T, R, I>)(Object)p;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public I andSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_AND);
		return (I)this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R, I> or(Function<T, R> property) {
		PredicateImpl<T, R, I, B> p = new PredicateImpl<T, R, I, B>((B) this, property, Type.OR);
		predicates.add((PredicateImpl<T, Object, I, B>) p);
		return (Predicate<T, R, I>)(Object)p;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public I orSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_OR);
		return (I)this;
	}
	
}
