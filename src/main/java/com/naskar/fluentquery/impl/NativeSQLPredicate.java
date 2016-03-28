package com.naskar.fluentquery.impl;

import java.util.ArrayList;
import java.util.List;

import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;

public class NativeSQLPredicate<T, R> implements Predicate<T, R> {

	private String name;
	private List<StringBuilder> conditions;
	
	public NativeSQLPredicate(String name) {
		this.name = name;
		this.conditions = new ArrayList<StringBuilder>();
	}
	
	private StringBuilder appendValue(String op, R value) {
		
		StringBuilder sb = new StringBuilder(name);
		sb.append(op);
		
		if(value instanceof String) {
			sb.append("'");
			sb.append((String)value);
			sb.append("'");
			
		} else {
			sb.append(value.toString());
		}
		
		return sb;
	}
	
	public List<StringBuilder> getConditions() {
		return conditions;
	}
	
	@Override
	public Query<T> eq(R value) {
		conditions.add(appendValue(" = ", value));
		return null;
	}
	
	@Override
	public Query<T> like(R value) {
		conditions.add(appendValue(" like ", value));
		return null;
	}

}
