package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Value;

public class IntoImpl<T> implements Into<T> {
	
	private Class<T> clazz;
	private List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values;

	public IntoImpl(Class<T> clazz) {
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
	public <E> E to(IntoConverter<E> converter) {
		return converter.convert(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> Value<Into<T>, R> value(Function<T, R> property) {
		ValueImpl<Into<T>, R> v = new ValueImpl<Into<T>, R>(this);
		Tuple<Function<T, R>, Value<Into<T>, R>> t = new Tuple<Function<T, R>, Value<Into<T>, R>>(property, v);
		// TODO: analisar porque
		this.values.add((Tuple<Function<T, ?>, Value<Into<T>, ?>>)(Object)t);
		return v;
	}	
	
}
