package com.naskar.fluentquery.impl;

import com.naskar.fluentquery.Value;

public class ValueImpl<T, R> implements Value<T, R> {
	
	private T t;
	private R value;
	
	public ValueImpl(T t) {
		this.t = t;
	}

	public R get() {
		return value;
	}
	
	@Override
	public T set(R value) {
		this.value = value;
		return t;
	}
	
}
