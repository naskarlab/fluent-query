package com.naskar.fluentquery;

import java.util.function.Function;

import com.naskar.fluentquery.impl.IntoConverter;

public interface Into<T> {
	
	Class<T> getClazz();
	
	<E> E to(IntoConverter<E> converter);
	
	<R> Value<Into<T>, R> value(Function<T, R> property);

}
