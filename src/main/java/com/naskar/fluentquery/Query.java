package com.naskar.fluentquery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.naskar.fluentquery.impl.QueryConverter;

public interface Query<T> {
	
	Class<T> getClazz();
	
	<E> E to(QueryConverter<E> converter);
	
	<R> Query<T> select(Function<T, R> property);
	
	<R> Query<T> select(Function<T, R> property, Consumer<Select> action);
	
	<J> Query<T> from(Class<J> clazz, BiConsumer<Query<J>, T> action);

	<R> Predicate<T, R, Query<T>> where(Function<T, R> property);

	<R> Predicate<T, R, Query<T>> and(Function<T, R> property);
	
	<R> Predicate<T, R, Query<T>> andIf(Supplier<Boolean> callIf, Function<T, R> property);
	
	<R> Predicate<T, R, Query<T>> or(Function<T, R> property);
	
	Query<T> whereSpec(Consumer<Query<T>> query);
	
	Query<T> andSpec(Consumer<Query<T>> query);
	
	Query<T> orSpec(Consumer<Query<T>> query);
	
	<R> Query<T> groupBy(Function<T, R> property);
	
	<R> OrderBy<Query<T>> orderBy(Function<T, R> property);
	
}
