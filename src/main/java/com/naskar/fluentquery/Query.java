package com.naskar.fluentquery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.naskar.fluentquery.impl.Converter;

public interface Query<T> {
	
	Class<T> getClazz();
	
	<E> E to(Converter<E> converter);
	
	<R> Query<T> select(Function<T, R> property);
	
	<J> Query<T> from(Class<J> clazz, BiConsumer<Query<J>, T> action);

	<R> Predicate<T, R> where(Function<T, R> property);

	<R> Predicate<T, R> and(Function<T, R> property);
	
	<R> Predicate<T, R> or(Function<T, R> property);
	
	Query<T> whereSpec(Consumer<Query<T>> query);
	
	Query<T> andSpec(Consumer<Query<T>> query);
	
	Query<T> orSpec(Consumer<Query<T>> query);
	
	<R> OrderBy<T> orderBy(Function<T, R> property);
}
