package com.naskar.fluentquery.impl;

import java.lang.reflect.Method;
import java.util.List;

public interface Convention {
	
	String getNameFromMethod(List<Method> methods);

	String getNameFromMethod(Method m);

	<T> String getNameFromClass(Class<T> clazz);
	
}
