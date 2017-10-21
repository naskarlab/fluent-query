package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.naskar.fluentquery.OrderBy;
import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.Select;
import com.naskar.fluentquery.converters.PredicateProvider;
import com.naskar.fluentquery.impl.PredicateImpl.Type;

// TODO: query bind dos parametros
public class QueryImpl<T> implements Query<T>, PredicateProvider<T, QueryImpl<T>> {
	
	private Class<T> clazz;
	private List<Function<T, ?>> selects;
	private Map<Function<T, ?>, Consumer<Select>> selectFunctions;
	private List<PredicateImpl<T, Object, QueryImpl<T>>> predicates;
	private List<Tuple<QueryImpl<?>, Consumer<T>>> froms;
	private List<GroupByImpl> groups;
	private List<OrderByImpl<?>> orders;

	public QueryImpl(Class<T> clazz) {
		this.clazz = clazz;
		this.predicates = new ArrayList<PredicateImpl<T, Object, QueryImpl<T>>>();
		this.selects = new ArrayList<Function<T, ?>>();
		this.selectFunctions = new HashMap<Function<T, ?>, Consumer<Select>>();
		this.groups = new ArrayList<GroupByImpl>();
		this.orders = new ArrayList<OrderByImpl<?>>();
		this.froms = new ArrayList<Tuple<QueryImpl<?>, Consumer<T>>>();
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	public List<Function<T, ?>> getSelects() {
		return selects;
	}
	
	public Map<Function<T, ?>, Consumer<Select>> getSelectFunctions() {
		return selectFunctions;
	}
	
	public List<GroupByImpl> getGroups() {
		return groups;
	}
	
	public List<OrderByImpl<?>> getOrders() {
		return orders;
	}
	
	public List<Tuple<QueryImpl<?>, Consumer<T>>> getFroms() {
		return froms;
	}
	
	public List<PredicateImpl<T, Object, QueryImpl<T>>> getPredicates() {
		return predicates;
	}
	
	@Override
	public <E> E to(QueryConverter<E> converter) {
		return converter.convert(this);
	}
	
	@Override
	public <R> Query<T> select(Function<T, R> property) {
		this.selects.add(property);
		return this;
	}
	
	@Override
	public <R> Query<T> select(Function<T, R> property, Consumer<Select> action) {
		this.selects.add(property);
		this.selectFunctions.put(property, action);
		return this;
	}
	
	@Override
	public <J> Query<T> from(Class<J> clazz, BiConsumer<Query<J>, T> action) {
		QueryImpl<J> queryImpl = new QueryImpl<J>(clazz);
		froms.add(new Tuple<QueryImpl<?>, Consumer<T>>(queryImpl, t -> action.accept(queryImpl, t)));
		return this;
	}

	@Override
	public <R> Predicate<T, R, Query<T>> where(Function<T, R> property) {
		return and(property);
	}
	
	@Override
	public Query<T> whereSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_AND);
		return this;
	}
	
	private void addSpec(Consumer<Query<T>> query, Type type) {
		QueryImpl<T> queryImpl = new QueryImpl<T>(clazz);
		
		PredicateImpl<T, Object, QueryImpl<T>> p = new PredicateImpl<T, Object, QueryImpl<T>>(this, i -> {
			query.accept(queryImpl);
			return queryImpl;
		}, type);
		
		predicates.add((PredicateImpl<T, Object, QueryImpl<T>>) p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R, Query<T>> and(Function<T, R> property) {
		PredicateImpl<T, R, QueryImpl<T>> p = new PredicateImpl<T, R, QueryImpl<T>>(this, property, Type.AND);
		predicates.add((PredicateImpl<T, Object, QueryImpl<T>>) p);
		return (Predicate<T, R, Query<T>>)(Object)p;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R, Query<T>> andIf(Supplier<Boolean> callIf, Function<T, R> property) {
		PredicateImpl<T, R, QueryImpl<T>> p = new PredicateImpl<T, R, QueryImpl<T>>(this, property, Type.AND);
		if(callIf.get()) {
			predicates.add((PredicateImpl<T, Object, QueryImpl<T>>)p);
		}
		return (Predicate<T, R, Query<T>>)(Object)p;
	}
	
	@Override
	public Query<T> andSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_AND);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Predicate<T, R, Query<T>> or(Function<T, R> property) {
		PredicateImpl<T, R, QueryImpl<T>> p = new PredicateImpl<T, R, QueryImpl<T>>(this, property, Type.OR);
		predicates.add((PredicateImpl<T, Object, QueryImpl<T>>) p);
		return (Predicate<T, R, Query<T>>)(Object)p;
	}
	
	@Override
	public Query<T> orSpec(Consumer<Query<T>> query) {
		addSpec(query, Type.SPEC_OR);
		return this;
	}
	
	@Override
	public <R> Query<T> groupBy(Function<T, R> property) {
		GroupByImpl o = new AttributeGroupByImpl<T, R>(property);
		this.groups.add(o);
		return this;
	}
	
	void groupBy(String alias) {
		GroupByImpl o = new AliasGroupByImpl(alias);
		this.groups.add(o);
	}
	
	@Override
	public <R> OrderBy<Query<T>> orderBy(Function<T, R> property) {
		return orderBy(this, property);
	}

	<I> OrderBy<I> orderBy(I target, String alias) {
		OrderByImpl<I> o = new AliasOrderByImpl<I>(target, alias);
		this.orders.add(o);
		return o;
	}
	
	<I, R> OrderBy<I> orderBy(I target, Function<T, R> property) {
		OrderByImpl<I> o = new AttributeOrderByImpl<I, T, R>(target, property);
		this.orders.add(o);
		return o;
	}
	
}
