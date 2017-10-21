package com.naskar.fluentquery.converters;

import java.util.ArrayList;
import java.util.List;

import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;

class NativeSQLPredicate<T, R> implements Predicate<T, R> {

	private String name;
	private List<StringBuilder> conditions;
	private List<String> parents;
	private NativeSQLResult result;
	
	public NativeSQLPredicate(String name, NativeSQLResult result) {
		this.name = name;
		this.result = result;
		this.conditions = new ArrayList<StringBuilder>();
	}
	
	public void setParents(List<String> parents) {
		this.parents = parents;
	}
	
	private StringBuilder appendValue(String op, R value) {
		
		StringBuilder sb = new StringBuilder(name);
		sb.append(op);
		
		if(value == null && parents != null && !parents.isEmpty()) {
			
			sb.append(parents.remove(0));
			
		} else {
			
			String p = result.add(value);
			sb.append(":");
			sb.append(p);
		
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
	public Query<T> ne(R value) {
		conditions.add(appendValue(" <> ", value));
		return null;
	}

	@Override
	public Query<T> gt(R value) {
		conditions.add(appendValue(" > ", value));
		return null;
	}
	
	@Override
	public Query<T> ge(R value) {
		conditions.add(appendValue(" >= ", value));
		return null;
	}
	
	@Override
	public Query<T> lt(R value) {
		conditions.add(appendValue(" < ", value));
		return null;
	}
	
	@Override
	public Query<T> le(R value) {
		conditions.add(appendValue(" <= ", value));
		return null;
	}
	
	@Override
	public Query<T> like(R value) {
		conditions.add(appendValue(" like ", value));
		return null;
	}
	
	@Override
	public Query<T> isNull() {
		conditions.add(new StringBuilder(" is null "));
		return null;
	}
	
	@Override
	public Query<T> isNotNull() {
		conditions.add(new StringBuilder(" is not null "));
		return null;
	}

}
