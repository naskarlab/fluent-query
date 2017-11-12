package com.naskar.fluentquery.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.naskar.fluentquery.impl.MethodRecordProxy;

public class Mapping<T> {
	
	private Class<T> clazz;
	private String tableName;
	
	private Map<List<Method>, String> columns;
	
	private MethodRecordProxy<T> proxy;

	public Mapping<T> to(Class<T> clazz, String tableName) {
		this.clazz = clazz;
		this.tableName = tableName;
		this.proxy = new MethodRecordProxy<T>(clazz);
		this.columns = new HashMap<List<Method>, String>();
		return this;
	}
	
	public Mapping<T> map(Consumer<T> action, String columnName) {
		proxy.clear();
		action.accept(proxy.getProxy());
		columns.put(new ArrayList<Method>(proxy.getMethods()), columnName);
		return this;
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public String getColumnName(List<Method> key) {
		return this.columns.get(key);
	}
	
	protected MethodRecordProxy<T> getProxy() {
		return proxy;
	}
	
	protected Map<List<Method>, String> getColumns() {
		return columns;
	}

}
