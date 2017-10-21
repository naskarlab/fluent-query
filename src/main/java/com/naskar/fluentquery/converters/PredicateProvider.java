package com.naskar.fluentquery.converters;

import java.util.List;

import com.naskar.fluentquery.impl.PredicateImpl;

public interface PredicateProvider<T, E> {
	
	List<PredicateImpl<T, Object, E>> getPredicates();

}
