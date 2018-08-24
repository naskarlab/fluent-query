package com.naskar.fluentquery.impl;

import java.lang.reflect.Method;
import java.util.List;

public interface MethodRecordProxy<T> {
	
	Method getCalledMethod();
	
	T getProxy();

	void clear();

	List<Method> getMethods();

	void setExecute(boolean b);
	
}