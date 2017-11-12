package com.naskar.fluentquery.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.naskar.fluentquery.impl.MethodRecordProxy;

public class MappingValueProvider<T> extends Mapping<T> {
	
	private Map<String, BiConsumer<T, ValueProvider>> attributes;
	
	public interface ValueProvider {
		<R> R get(String name, Class<R> clazz);
	}
	
	public MappingValueProvider() {
		this.attributes = new HashMap<String, BiConsumer<T, ValueProvider>>();
	}
	
	public MappingValueProvider<T> to(Class<T> clazz, String tableName) {
		to(clazz, tableName);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <R> MappingValueProvider<T> map(Function<T, R> getter, String columnName, BiConsumer<T, R> setter) {
		
		MethodRecordProxy<T> proxy = getProxy();
		Map<List<Method>, String> columns = getColumns();
		
		proxy.clear();
		getter.apply(proxy.getProxy());
		columns.put(new ArrayList<Method>(proxy.getMethods()), columnName);
		
		Class<?> returnType = proxy.getCalledMethod().getReturnType();
		this.attributes.put(columnName, (T t, ValueProvider provider) -> {
			setter.accept(t, (R) provider.get(columnName, returnType));
		});
		
		return this;
	}
	
	public void fill(T t, ValueProvider provider) {
		this.attributes.forEach((k,v) -> v.accept(t, provider));
	}

	
}
