package com.naskar.fluentquery;

import java.util.function.Function;

public interface Query<T> {

	<R> Predicate<T, R> where(Function<T, R> property);

	<R> Predicate<T, R> and(Function<T, R> property);

	<R> Query<T> select(Function<T, R> property);

	<E> E to(Converter<E> converter);

}
