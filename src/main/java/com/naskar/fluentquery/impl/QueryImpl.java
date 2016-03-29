package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.impl.PredicateImpl.Type;

public class QueryImpl<T> implements Query<T> {
	
	private Class<T> clazz;
	private List<Function<T, ?>> selects;
	private List<PredicateImpl<T, Object>> predicates;
	private List<Tuple<QueryImpl<?>, Consumer<T>>> froms;

	public QueryImpl(Class<T> clazz) {
		this.clazz = clazz;
		this.predicates = new ArrayList<PredicateImpl<T, Object>>();
		this.selects = new ArrayList<Function<T, ?>>();
		this.froms = new ArrayList<Tuple<QueryImpl<?>, Consumer<T>>>();
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	public List<Function<T, ?>> getSelects() {
		return selects;
	}
	
	public List<Tuple<QueryImpl<?>, Consumer<T>>> getFroms() {
		return froms;
	}
	
	public List<PredicateImpl<T, Object>> getPredicates() {
		return predicates;
	}
	
	@Override
	public <E> E to(Converter<E> converter) {
		return converter.convert(this);
	}
	
	@Override
	public <R> Query<T> select(Function<T, R> property) {
		this.selects.add(property);
		return this;
	}
	
	@Override
	public <J> Query<T> from(Class<J> clazz, BiConsumer<Query<J>, T> action) {
		QueryImpl<J> queryImpl = new QueryImpl<J>(clazz);
		froms.add(new Tuple<QueryImpl<?>, Consumer<T>>(queryImpl, t -> action.accept(queryImpl, t)));
		return this;
	}

	@Override
	public <R> Predicate<T, R> where(Function<T, R> property) {
		return and(property);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R> and(Function<T, R> property) {
		PredicateImpl<T, R> p = new PredicateImpl<T, R>(this, property, Type.AND);
		predicates.add((PredicateImpl<T, Object>) p);
		return p;
	}

}
