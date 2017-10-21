package com.naskar.fluentquery.converters;

import java.util.List;

import com.naskar.fluentquery.impl.PredicateImpl;

public interface PredicateProvider<T, B> {
	
	<I> List<PredicateImpl<T, Object, I, B>> getPredicates();

}
