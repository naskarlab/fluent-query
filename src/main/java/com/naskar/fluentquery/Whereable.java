package com.naskar.fluentquery;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Whereable<T, I> {
	
	<R> Predicate<T, R, I> where(Function<T, R> property);

	<R> Predicate<T, R, I> and(Function<T, R> property);
	
	<R> Predicate<T, R, I> andIf(Supplier<Boolean> callIf, Function<T, R> property);
	
	<R> Predicate<T, R, I> or(Function<T, R> property);
	
	I whereSpec(Consumer<Query<T>> query);
	
	I andSpec(Consumer<Query<T>> query);
	
	I orSpec(Consumer<Query<T>> query);	
	
}
