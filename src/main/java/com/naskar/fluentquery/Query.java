package com.naskar.fluentquery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.naskar.fluentquery.impl.QueryConverter;

public interface Query<T> extends Whereable<T, Query<T>> {
	
	Class<T> getClazz();
	
	<E> E to(QueryConverter<E> converter);
	
	<R> Query<T> select(Function<T, R> property);
	
	<R> Query<T> select(Function<T, R> property, Consumer<Select> action);
	
	<J> Query<T> from(Class<J> clazz, BiConsumer<Query<J>, T> action);
	
	<R> Query<T> groupBy(Function<T, R> property);
	
	<R> OrderBy<Query<T>> orderBy(Function<T, R> property);
	
}
