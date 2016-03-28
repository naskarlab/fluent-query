package com.naskar.fluentquery;

import java.util.List;
import java.util.function.Function;

import com.naskar.fluentquery.impl.PredicateImpl;

public interface Converter<E> {

	<T> E convert(Class<T> clazz, List<Function<T, ?>> selects, List<PredicateImpl<T, Object>> predicates);

}
