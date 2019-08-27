package com.naskar.fluentquery;

@FunctionalInterface
public interface Join<J, T> {
	
	void accept(Query<J> q, T parent);

}
