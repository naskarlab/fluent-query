package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Update;
import com.naskar.fluentquery.Value;
import com.naskar.fluentquery.converters.PredicateProvider;

public class UpdateImpl<T>
	extends WhereImpl<T, Update<T>, UpdateImpl<T>> 
	implements Update<T>, PredicateProvider<T, UpdateImpl<T>> {
	
	private Class<T> clazz;
	private List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values;

	public UpdateImpl(Class<T> clazz) {
		super(clazz);
		this.clazz = clazz;
		this.values = new ArrayList<Tuple<Function<T, ?>, Value<Into<T>, ?>>>();
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	public List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> getValues() {
		return values;
	}
	
	@Override
	public <E> E to(UpdateConverter<E> converter) {
		return converter.convert(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Value<Update<T>, R> value(Function<T, R> property) {
		ValueImpl<Update<T>, R> v = new ValueImpl<Update<T>, R>(this);
		Tuple<Function<T, R>, Value<Update<T>, R>> t = new Tuple<Function<T, R>, Value<Update<T>, R>>(property, v);
		// TODO: analisar porque
		this.values.add((Tuple<Function<T, ?>, Value<Into<T>, ?>>)(Object)t);
		return v;
	}

}
