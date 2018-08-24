package com.naskar.fluentquery.impl;

public interface MethodRecordProxyFactory {
	
	<T> MethodRecordProxy<T> create(Class<T> clazz);
		
}
