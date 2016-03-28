package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.naskar.fluentquery.Converter;
import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.impl.PredicateImpl.Type;

public class QueryImpl<T> implements Query<T> {
	
	private Class<T> clazz;
	private List<Function<T, ?>> selects;
	private List<PredicateImpl<T, Object>> predicates;

	public QueryImpl(Class<T> clazz) {
		this.clazz = clazz;
		this.predicates = new ArrayList<PredicateImpl<T, Object>>();
		this.selects = new ArrayList<Function<T, ?>>();
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

	@Override
	public <R> Query<T> select(Function<T, R> property) {
		this.selects.add(property);
		return this;
	}

	@Override
	public <E> E to(Converter<E> converter) {
		return converter.convert(clazz, selects, predicates);
	}

}
