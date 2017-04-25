package com.naskar.fluentquery.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeSQLResult {
	
	private String sql;
	private Map<String, Object> params;
	private List<Object> values;
	private int i;
	
	public NativeSQLResult() {
		this.i = -1;
		this.params = new HashMap<String, Object>();
		this.values = new ArrayList<Object>();
	}
	
	public NativeSQLResult sql(String value) {
		this.sql = value;
		return this;
	}
	
	public String sql() {
		return sql;
	}
	
	public String sqlValues() {
		String temp = sql;
		for(String k : params.keySet()) {
			temp = temp.replaceAll(":" + k, "?");
		}
		return temp;
	}
	
	public Map<String, Object> params() {
		return params;
	}
	
	public List<Object> values() {
		return values;
	}
	
	// TODO: bug: chaves usadas em dois locais
	String add(Object value) {
		String k = "p" + (++i);
		params.put(k, value);
		values.add(value);
		return k;
	}

}