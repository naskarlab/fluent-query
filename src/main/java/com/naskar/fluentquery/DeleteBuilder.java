package com.naskar.fluentquery;

import com.naskar.fluentquery.impl.DeleteImpl;

public class DeleteBuilder {

	public <T> Delete<T> entity(Class<T> clazz) {
		return new DeleteImpl<T>(clazz);
	}

}
