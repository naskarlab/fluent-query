package com.naskar.fluentquery.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeSQLResult {
	
	private String sql;
	private Map<String, Object> params;
	private List<String> names;
	private List<Object> values;
	private int i;
	
	public NativeSQLResult(Integer level) {
		this.i = level;
		this.params = new HashMap<String, Object>();
		this.names = new ArrayList<String>();
		this.values = new ArrayList<Object>();
	}
	
	public NativeSQLResult() {
		this(-1);
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
		for(int i = names.size()-1; i > -1; i--) {
			String k = names.get(i);
			temp = temp.replaceAll(":" + k, "?");
		}
		return temp;
	}
	
	public List<String> names() {
		return names;
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
		names.add(k);
		values.add(value);
		return k;
	}

	public String addResult(NativeSQLResult result) {
		String temp = result.sql();
		
		for(int i = 0; i < result.values.size(); i++) {
			
			String newP = this.add(result.values.get(i));
			String oldP = result.names.get(i);
			temp = temp.replaceAll(":" + oldP, ":" + newP);
			
		}
		
		return temp;
	}

}