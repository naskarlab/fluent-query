//package com.naskar.fluentquery.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
//import com.naskar.fluentquery.Into;
//import com.naskar.fluentquery.Predicate;
//import com.naskar.fluentquery.Update;
//import com.naskar.fluentquery.Value;
//import com.naskar.fluentquery.converters.PredicateProvider;
//
//public class UpdateImpl<T> implements Update<T>, PredicateProvider<T, UpdateImpl<T>> {
//	
//	private Class<T> clazz;
//	private List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values;
//	private List<PredicateImpl<T, Object, UpdateImpl<T>>> predicates;
//
//	public UpdateImpl(Class<T> clazz) {
//		this.clazz = clazz;
//		this.values = new ArrayList<Tuple<Function<T, ?>, Value<Into<T>, ?>>>();
//		this.predicates = new ArrayList<PredicateImpl<T, Object, UpdateImpl<T>>>();
//	}
//	
//	public Class<T> getClazz() {
//		return clazz;
//	}
//	
//	public List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> getValues() {
//		return values;
//	}
//	
//	public List<PredicateImpl<T, Object, UpdateImpl<T>>> getPredicates() {
//		return predicates;
//	}
//	
//	@Override
//	public <E> E to(UpdateConverter<E> converter) {
//		return converter.convert(this);
//	}
//
//	@Override
//	public <R> Value<Update<T>, R> value(Function<T, R> property) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <R> Predicate<T, R, Update<T>> where(Function<T, R> property) {
//		// TODO Auto-generated method stub
//		return null;
//	}	
//}
