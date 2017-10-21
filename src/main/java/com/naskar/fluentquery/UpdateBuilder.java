package com.naskar.fluentquery;

import com.naskar.fluentquery.impl.UpdateImpl;

public class UpdateBuilder {

	public <T> Update<T> entity(Class<T> clazz) {
		return new UpdateImpl<T>(clazz);
	}

}
