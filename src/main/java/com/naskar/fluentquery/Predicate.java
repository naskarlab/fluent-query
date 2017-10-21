package com.naskar.fluentquery;

public interface Predicate<T, R, B> {

	B eq(R value);
	
	B ne(R value);

	B gt(R value);
	
	B ge(R value);

	B lt(R value);
	
	B le(R value);
	
	B like(R value);
	
	B isNull();
	
	B isNotNull();

}
