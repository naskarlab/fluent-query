package com.naskar.fluentquery.conventions;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.mapping.Mapping;

public class MappingConvention implements Convention {
	
	private Map<Class<?>, Mapping<?>> maps;
	
	public MappingConvention() {
		this.maps = new HashMap<Class<?>, Mapping<?>>();
	}
	
	public <T> void add(Mapping<T> map) {
		maps.put(map.getClazz(), map);
	}
	
	@Override
	public String getNameFromMethod(Method m) {
		return getNameFromMethod(Arrays.asList(m));
	}

	@Override
	public String getNameFromMethod(List<Method> methods) {
		String name = null;
		
		if(!methods.isEmpty()) {
			Mapping<?> mapping = maps.get(methods.get(0).getDeclaringClass());
			if(mapping != null) {
				name = mapping.getColumnName(methods);
			}
		}
		
		return name;
	}
	
	@Override
	public <T> String getNameFromClass(Class<T> clazz) {
		String name = null;
		
		Mapping<?> mapping = maps.get(clazz);
		if(mapping != null) {
			name = mapping.getTableName();
		}
		
		return name;
	}
	
}
