package com.naskar.fluentquery;

public interface Predicate<T, R> {

	Query<T> eq(R value);
	
	Query<T> ne(R value);

	Query<T> gt(R value);
	
	Query<T> ge(R value);

	Query<T> lt(R value);
	
	Query<T> le(R value);
	
	Query<T> like(R value);
	
	Query<T> isNull();
	
	Query<T> isNotNull();

}
