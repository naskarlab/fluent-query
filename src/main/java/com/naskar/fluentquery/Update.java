//package com.naskar.fluentquery;
//
//import java.util.function.Function;
//
//import com.naskar.fluentquery.impl.UpdateConverter;
//
//public interface Update<T> {
//	
//	Class<T> getClazz();
//	
//	<E> E to(UpdateConverter<E> converter);
//	
//	<R> Value<Update<T>, R> value(Function<T, R> property);
//	
//	<R> Predicate<T, R> where(Function<T, R> property);
//
//}
