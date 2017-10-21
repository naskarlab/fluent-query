//package com.naskar.fluentquery.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
//import com.naskar.fluentquery.Into;
//import com.naskar.fluentquery.Value;
//
//public class UpdateImpl<T> implements Update<T> {
//	
//	private Class<T> clazz;
//	private List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values;
//
//	public UpdateImpl(Class<T> clazz) {
//		this.clazz = clazz;
//		this.values = new ArrayList<Tuple<Function<T, ?>, Value<Into<T>, ?>>>();
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
//	@Override
//	public <E> E to(UpdateConverter<E> converter) {
//		return converter.convert(this);
//	}	
//}
