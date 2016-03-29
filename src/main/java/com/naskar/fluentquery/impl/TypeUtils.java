package com.naskar.fluentquery.impl;

import java.util.HashSet;
import java.util.Set;

public class TypeUtils {

	private static final Set<Class<?>> VALUE_TYPE = getWrapperTypes();

	public static boolean isValueType(Class<?> clazz) {
		return VALUE_TYPE.contains(clazz);
	}

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<Class<?>>();
		
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		ret.add(String.class);
		
		return ret;
	}

}
