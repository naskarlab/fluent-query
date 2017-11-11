package com.naskar.fluentquery.binder;

public class BinderSQLBuilder {

	public <T> BinderSQL<T> from(Class<T> clazz) {
		return new BinderSQLImpl<T>(clazz);
	}

}
