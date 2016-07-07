package com.naskar.fluentquery.converters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;

class NativeSQLPredicate<T, R> implements Predicate<T, R> {

	private String name;
	private List<StringBuilder> conditions;
	private List<String> parents;
	
	private SimpleDateFormat sdf;
	
	public NativeSQLPredicate(String name) {
		this.name = name;
		this.conditions = new ArrayList<StringBuilder>();
		this.sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss"); // SQL ANSI
	}
	
	public void setParents(List<String> parents) {
		this.parents = parents;
	}
	
	private StringBuilder appendValue(String op, R value) {
		
		StringBuilder sb = new StringBuilder(name);
		sb.append(op);
		
		if(value == null) {

			if(parents.isEmpty()) {
				throw new IllegalArgumentException();
			}
			
			sb.append(parents.remove(0));
			
		} else {
		
			if(value instanceof String) {
				sb.append("'");
				sb.append((String)value);
				sb.append("'");
				
			} else if(value instanceof Date) {
				sb.append("'");
				sb.append(sdf.format((Date)value));
				sb.append("'");
				
			} else {
				sb.append(value.toString());
			}
			
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
