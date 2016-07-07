package com.naskar.fluentquery;

public interface OrderBy<T> {

	Query<T> asc();
	
	Query<T> desc();

}
