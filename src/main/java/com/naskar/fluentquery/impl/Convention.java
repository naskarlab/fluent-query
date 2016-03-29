package com.naskar.fluentquery.impl;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Convention {
	
	public static String getNameFromMethod(List<Method> methods) {
		StringBuilder sb = new StringBuilder();
		
		for(Method m : methods) {
			String name = Convention.getNameFromMethod(m);
			if(sb.length() > 0) {
				sb.append("_");
			}
			sb.append(name);
		}
		
		return sb.toString();
	}

	public static String getNameFromMethod(Method m) {
		String result = m.getName();
		
		String name = m.getName();
		if((name.startsWith("get") || name.startsWith("set"))
				&& name.length() > 3) {
			
			result = name.substring(3, 4).toLowerCase();
			if(name.length() > 4) {
				result += name.substring(4);
			}
		}
		
		return result;
	}

	public static <T> String getNameFromClass(Class<T> clazz) {
		return clazz.getSimpleName();
	}
	
}
