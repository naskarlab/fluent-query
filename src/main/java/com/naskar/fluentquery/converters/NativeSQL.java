package com.naskar.fluentquery.converters;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.Converter;
import com.naskar.fluentquery.impl.HolderInt;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.PredicateImpl;
import com.naskar.fluentquery.impl.QueryImpl;
import com.naskar.fluentquery.impl.QueryParts;

public class NativeSQL implements Converter<String> {
	
	private Convention convention;
	
	public NativeSQL(Convention convention) {
		this.convention = convention;
	}
	
	public NativeSQL() {
		this(new SimpleConvention());
	}
	
	@Override
	public <T> String convert(QueryImpl<T> queryImpl) {
		
		QueryParts parts = new QueryParts();
		
		HolderInt level = new HolderInt();
		level.value = 0;
		
		convert(queryImpl, parts, level, null);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("select ");
		sb.append(parts.getSelect());
		
		sb.append(" from ");
		sb.append(parts.getFrom());
		
		if(parts.hasWhere()) {
			sb.append(" where ");
			sb.append(parts.getWhere());
		}
		
		return sb.toString();
	}
	
	private <T> void convert(QueryImpl<T> queryImpl, QueryParts parts, final HolderInt level, List<String> parents) {
		MethodRecordProxy<T> proxy = new MethodRecordProxy<T>(
				createInstance(queryImpl.getClazz()));
		
		String alias = "e" + level + ".";
		
		convertSelect(parts.getSelect(), alias, proxy, queryImpl.getSelects());
		convertFrom(parts.getFrom(), alias, queryImpl.getClazz());
		convertWhere(parts.getWhere(), alias, proxy, parents, queryImpl.getPredicates());
		
		queryImpl.getFroms().forEach(i -> {
			
			proxy.clear();
			i.getT2().accept(proxy.getProxy());
			
			List<String> parentsTmp = new ArrayList<String>();
			for(Method m : proxy.getMethods()) {
				parentsTmp.add(alias + convention.getNameFromMethod(m));
			}
			
			level.value++;
			convert(i.getT1(), parts, level, parentsTmp);
			
		});
		
		level.value += 10;
	}
	
	private <T> T createInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> void convertSelect(
		StringBuilder sb, String alias,
		MethodRecordProxy<T> proxy, 
		List<Function<T, ?>> selects) {
		
		String s = selects.stream().map(i -> {
			
			proxy.clear();
			i.apply(proxy.getProxy());
			
			return alias + convention.getNameFromMethod(proxy.getCalledMethod());
			
		}).collect(Collectors.joining(", "));
		
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(s.isEmpty() ? alias + "*" : s);
	}
	
	private <T> void convertFrom(
			StringBuilder sb, String alias, Class<T> clazz) {
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(convention.getNameFromClass(clazz) + " " + 
			alias.substring(0, alias.length()-1));
	}
	
	private <T> void convertWhere(
			StringBuilder sb, 
			String alias,
			MethodRecordProxy<T> proxy,
			List<String> parents,
			List<PredicateImpl<T, Object>> predicates) {
		
		List<StringBuilder> conditions = new ArrayList<StringBuilder>();
		
		predicates.stream().forEach(p -> {
			
			proxy.clear();
			p.getProperty().apply(proxy.getProxy());
			
			String name = 
				alias +	convention.getNameFromMethod(proxy.getMethods());
			
			p.getActions().forEach(action -> {
				
				NativeSQLPredicate<T, Object> predicate = new NativeSQLPredicate<T, Object>(name);
				predicate.setParents(parents);
				action.accept(predicate);
				
				predicate.getConditions().stream().forEach(cond -> {
				
					// TODO: AND
					conditions.add(cond);
					
				});
				
			});
			
		});
		
		if(sb.length() > 0) {
			sb.append(" and ");
		}
		
		sb.append(conditions.stream()
			.map(i -> i.toString())
			.collect(Collectors.joining(" and ")));
	}

}
