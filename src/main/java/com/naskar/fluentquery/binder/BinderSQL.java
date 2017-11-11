package com.naskar.fluentquery.binder;

import java.util.function.Function;

import com.naskar.fluentquery.converters.NativeSQLResult;

public interface BinderSQL<T> extends Binder<T, NativeSQLResult> {
	
	void configure(NativeSQLResult result);

	<R> R get(Function<T, R> getter);
	
	NativeSQLResult bind(T t);

}
