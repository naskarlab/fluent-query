package com.naskar.fluentquery.conventions;

import java.lang.reflect.Method;
import java.util.List;

import com.naskar.fluentquery.impl.Convention;

public class SimpleConvention implements Convention {
	
	@Override
	public String getNameFromMethod(List<Method> methods) {
		StringBuilder sb = new StringBuilder();
		
		for(Method m : methods) {
			String name = getNameFromMethod(m);
			if(sb.length() > 0) {
				sb.append("_");
			}
			sb.append(name);
		}
		
		return sb.toString();
	}

	@Override
	public String getNameFromMethod(Method m) {
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

	@Override
	public <T> String getNameFromClass(Class<T> clazz) {
		return clazz.getSimpleName();
	}
	
}
