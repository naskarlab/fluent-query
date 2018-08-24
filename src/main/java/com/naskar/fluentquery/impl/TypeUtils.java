package com.naskar.fluentquery.impl;

import java.util.HashSet;
import java.util.Set;

public class TypeUtils {
	
	private static MethodRecordProxyFactory recordFactory;
	
	static {
		recordFactory = new MethodRecordProxyFactory() {
			public <T> MethodRecordProxy<T> create(Class<T> clazz) {
				return new MethodRecordProxyCGLib<T>(clazz);
			}
		};
	}
	
	public static void setMethodRecordProxyFactory(MethodRecordProxyFactory f) {
		recordFactory = f;
	}
	
	public static <E> E createInstance(Class<E> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> MethodRecordProxy<T> createProxy(Class<T> clazz) {
		return recordFactory.create(clazz);
	}
	
	private TypeUtils () {
	}

	private static final Set<Class<?>> VALUE_TYPE = getValuesTypes();

	public static boolean isValueType(Class<?> clazz) {
		return VALUE_TYPE.contains(clazz);
	}

	private static Set<Class<?>> getValuesTypes() {
		Set<Class<?>> types = new HashSet<Class<?>>();
		
		types.add(Character.class);
		types.add(String.class);
		types.add(Boolean.class);
		types.add(Byte.class);
		types.add(Short.class);
		types.add(Integer.class);
		types.add(Long.class);
		types.add(Float.class);
		types.add(Double.class);
		types.add(Void.class);
		
		return types;
	}

}
