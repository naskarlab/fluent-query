package com.naskar.fluentquery.converters;

import java.util.ArrayList;
import java.util.List;

import com.naskar.fluentquery.Predicate;

class NativeSQLPredicate<T, R, I> implements Predicate<T, R, I> {

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
	public I eq(R value) {
		conditions.add(appendValue(" = ", value));
		return null;
	}
	
	@Override
	public I ne(R value) {
		conditions.add(appendValue(" <> ", value));
		return null;
	}

	@Override
	public I gt(R value) {
		conditions.add(appendValue(" > ", value));
		return null;
	}
	
	@Override
	public I ge(R value) {
		conditions.add(appendValue(" >= ", value));
		return null;
	}
	
	@Override
	public I lt(R value) {
		conditions.add(appendValue(" < ", value));
		return null;
	}
	
	@Override
	public I le(R value) {
		conditions.add(appendValue(" <= ", value));
		return null;
	}
	
	@Override
	public I like(R value) {
		conditions.add(appendValue(" like ", value));
		return null;
	}
	
	@Override
	public I isNull() {
		conditions.add(new StringBuilder(" is null "));
		return null;
	}
	
	@Override
	public I isNotNull() {
		conditions.add(new StringBuilder(" is not null "));
		return null;
	}

}
