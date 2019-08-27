package com.naskar.fluentquery.converters;

import java.util.ArrayList;
import java.util.List;

import com.naskar.fluentquery.Join;
import com.naskar.fluentquery.Predicate;
import com.naskar.fluentquery.Query;
import com.naskar.fluentquery.QueryBuilder;
import com.naskar.fluentquery.impl.HolderInt;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.QueryImpl;

class NativeSQLPredicate<T, R, I> implements Predicate<T, R, I> {

	private NativeSQL nativeSQL;
	private MethodRecordProxy<T> proxy;
	private String name;
	private NativeSQLResult result;

	private String alias;
	private HolderInt level;
	private List<String> parents;
	
	private List<StringBuilder> conditions;
	
	public NativeSQLPredicate(NativeSQL nativeSQL, MethodRecordProxy<T> proxy, 
			String name, NativeSQLResult result) {
		this.nativeSQL = nativeSQL;
		this.proxy = proxy;
		
		this.name = name;
		this.result = result;
		this.conditions = new ArrayList<StringBuilder>();
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public void setLevel(HolderInt level) {
		this.level = level;
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
		conditions.add(new StringBuilder(name).append(" is null "));
		return null;
	}
	
	@Override
	public I isNotNull() {
		conditions.add(new StringBuilder(name).append(" is not null "));
		return null;
	}
	
	@Override
	public <J> I in(Class<J> clazz, Join<J, T> action) {
		NativeSQLResult result = toSQL(clazz, action);
		
		conditions.add(new StringBuilder(name)
			.append(" in (")
			.append(result.sql())
			.append(")")
		);
		
		this.result.addResult(result);
		
		return null;
	}

	@Override
	public <J> I notIn(Class<J> clazz, Join<J, T> action) {
		NativeSQLResult result = toSQL(clazz, action);
		
		conditions.add(new StringBuilder(name)
			.append(" not in (")
			.append(result.sql())
			.append(")")
		);
		
		this.result.addResult(result);
		
		return null;
	}
	
	private <J> NativeSQLResult toSQL(Class<J> clazz, Join<J, T> action) {
		Query<J> q = new QueryBuilder().from(clazz);
		QueryImpl<J> qi = (QueryImpl<J>)q;
		
		HolderInt newLevel = new HolderInt();
		newLevel.value = (level.value + 1) * 1000;
		
		this.proxy.clear();
		action.accept(q, this.proxy.getProxy());
		
		List<String> parentsTemp = nativeSQL.createParents(alias, proxy);
		
		return nativeSQL.convert(qi, parentsTemp, newLevel);
	}

}
