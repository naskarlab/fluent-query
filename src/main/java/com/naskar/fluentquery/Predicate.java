package com.naskar.fluentquery;

public interface Predicate<T, R, I> {

	I eq(R value);
	
	I ne(R value);

	I gt(R value);
	
	I ge(R value);

	I lt(R value);
	
	I le(R value);
	
	I like(R value);
	
	I isNull();
	
	I isNotNull();
	
	<J> I in(Class<J> clazz, Join<J, T>	 action);
	
	<J> I notIn(Class<J> clazz, Join<J, T> action);

}
