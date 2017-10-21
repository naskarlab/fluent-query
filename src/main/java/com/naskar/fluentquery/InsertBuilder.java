package com.naskar.fluentquery;

import com.naskar.fluentquery.impl.IntoImpl;

public class InsertBuilder {

	public <T> Into<T> into(Class<T> clazz) {
		return new IntoImpl<T>(clazz);
	}

}
