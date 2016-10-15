package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.naskar.fluentquery.OrderBy;
import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.impl.PredicateImpl.Type;

public class QueryImpl<T> implements Query<T> {
	
	private Class<T> clazz;
	private List<Function<T, ?>> selects;
	private List<PredicateImpl<T, Object>> predicates;
	private List<Tuple<QueryImpl<?>, Consumer<T>>> froms;
	private List<OrderByImpl<T, ?>> orders;

	public QueryImpl(Class<T> clazz) {
		this.clazz = clazz;
		this.predicates = new ArrayList<PredicateImpl<T, Object>>();
		this.selects = new ArrayList<Function<T, ?>>();
		this.orders = new ArrayList<OrderByImpl<T, ?>>();
		this.froms = new ArrayList<Tuple<QueryImpl<?>, Consumer<T>>>();
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	public List<Function<T, ?>> getSelects() {
		return selects;
	}
	
	public List<OrderByImpl<T, ?>> getOrders() {
		return orders;
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
	
	@Override
	public Query<T> whereSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_AND);
		return this;
	}
	
	private void addSpec(Consumer<Query<T>> query, Type type) {
		QueryImpl<T> queryImpl = new QueryImpl<T>(clazz);
		
		PredicateImpl<T, Object> p = new PredicateImpl<T, Object>(this, i -> {
			query.accept(queryImpl);
			return queryImpl;
		}, type);
		
		predicates.add((PredicateImpl<T, Object>) p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R> and(Function<T, R> property) {
		PredicateImpl<T, R> p = new PredicateImpl<T, R>(this, property, Type.AND);
		predicates.add((PredicateImpl<T, Object>) p);
		return p;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R> andIf(Supplier<Boolean> callIf, Function<T, R> property) {
		PredicateImpl<T, R> p = new PredicateImpl<T, R>(this, property, Type.AND);
		if(callIf.get()) {
			predicates.add((PredicateImpl<T, Object>) p);
		}
		return p;
	}
	
	@Override
	public Query<T> andSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_AND);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R> or(Function<T, R> property) {
		PredicateImpl<T, R> p = new PredicateImpl<T, R>(this, property, Type.OR);
		predicates.add((PredicateImpl<T, Object>) p);
		return p;
	}
	
	@Override
	public Query<T> orSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_OR);
		return this;
	}
	
	@Override
	public <R> OrderBy<T> orderBy(Function<T, R> property) {
		OrderByImpl<T, R> o = new OrderByImpl<T, R>(this, property);
		this.orders.add(o);
		return o;
	}

}
