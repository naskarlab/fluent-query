package com.naskar.fluentquery;

import com.naskar.fluentquery.impl.QueryImpl;

public class QueryBuilder {

	public <T> Query<T> from(Class<T> clazz) {
		return new QueryImpl<T>(clazz);
	}

}
