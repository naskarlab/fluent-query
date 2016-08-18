package com.naskar.fluentquery.converters;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.Converter;
import com.naskar.fluentquery.impl.HolderInt;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.OrderByImpl;
import com.naskar.fluentquery.impl.PredicateImpl;
import com.naskar.fluentquery.impl.PredicateImpl.Type;
import com.naskar.fluentquery.impl.QueryImpl;
import com.naskar.fluentquery.impl.QueryParts;
import com.naskar.fluentquery.impl.TypeUtils;

public class NativeSQL implements Converter<NativeSQLResult> {
	
	private Convention convention;
	
	public NativeSQL(Convention convention) {
		this.convention = convention;
	}
	
	public NativeSQL() {
		this(new SimpleConvention());
	}
	
	@Override
	public <T> NativeSQLResult convert(QueryImpl<T> queryImpl) {
		
		NativeSQLResult result = new NativeSQLResult();
		
		QueryParts parts = new QueryParts();
		
		HolderInt level = new HolderInt();
		level.value = 0;
		
		convert(queryImpl, parts, level, result, null);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("select ");
		sb.append(parts.getSelect());
		
		sb.append(" from ");
		sb.append(parts.getFrom());
		
		if(parts.hasWhere()) {
			sb.append(" where ");
			sb.append(parts.getWhere());
		}
		
		if(parts.hasOrderBy()) {
			sb.append(" order by ");
			sb.append(parts.getOrderBy());
		}
		
		return result.sql(sb.toString());
	}
	
	private <T> void convert(QueryImpl<T> queryImpl, QueryParts parts, final HolderInt level, NativeSQLResult result, List<String> parents) {
		MethodRecordProxy<T> proxy = new MethodRecordProxy<T>(
				createInstance(queryImpl.getClazz()));
		
		String alias = "e" + level + ".";
		
		convertSelect(parts.getSelect(), alias, proxy, queryImpl.getSelects());
		convertFrom(parts.getFrom(), alias, queryImpl.getClazz());
		convertWhere(parts.getWhere(), alias, proxy, parents, queryImpl.getPredicates(), result);
		convertOrderBy(parts.getOrderBy(), alias, proxy, queryImpl.getOrders());
		
		queryImpl.getFroms().forEach(i -> {
			
			proxy.clear();
			i.getT2().accept(proxy.getProxy());
			
			List<String> parentsTmp = new ArrayList<String>();
			Iterator<Method> it = proxy.getMethods().iterator(); 
			while(it.hasNext()) {
				
				Method m = null;
				List<Method> path = new ArrayList<Method>();
				do {
					m = it.next();
					path.add(m);
				} while(!TypeUtils.isValueType(m.getReturnType()) && it.hasNext());
				
				parentsTmp.add(alias + convention.getNameFromMethod(path));
			}
			
			level.value++;
			convert(i.getT1(), parts, level, result, parentsTmp);
			
		});
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
	
	private <T> void convertOrderBy(
		StringBuilder sb, String alias,
		MethodRecordProxy<T> proxy, 
		List<OrderByImpl<T, ?>> orders) {
		
		String s = orders.stream().map(i -> {
			
			proxy.clear();
			i.getProperty().apply(proxy.getProxy());
			
			return 
				alias 
				+ convention.getNameFromMethod(proxy.getCalledMethod()) 
				+ (OrderByImpl.OrderByType.DESC.equals(i.getType()) ? " desc" : "");
			
		}).collect(Collectors.joining(", "));
		
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(s);
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
			List<PredicateImpl<T, Object>> predicates,
			NativeSQLResult result) {
		
		List<StringBuilder> conditions = new ArrayList<StringBuilder>();
		List<Type> conditionTypes = new ArrayList<Type>();
		
		predicates.stream().forEach(p -> {
			
			if(p.getType() == Type.SPEC_AND || p.getType() == Type.SPEC_OR) {
				
				StringBuilder sbSpec = new StringBuilder("");
				
				@SuppressWarnings("unchecked")
				QueryImpl<T> q = ((QueryImpl<T>)p.getProperty().apply(null));
				
				convertWhere(sbSpec, alias, proxy, parents, q.getPredicates(), result);
				if(sbSpec.length() > 0) {
					sbSpec.insert(0, "(");
					sbSpec.append(")");
					conditions.add(sbSpec);
					conditionTypes.add(p.getType());
				}
				
			} else {
			
				proxy.clear();
				p.getProperty().apply(proxy.getProxy());
				
				String name = 
					alias +	convention.getNameFromMethod(proxy.getMethods());
				
				p.getActions().forEach(action -> {
					
					NativeSQLPredicate<T, Object> predicate = new NativeSQLPredicate<T, Object>(name, result);
					predicate.setParents(parents);
					action.accept(predicate);
					
					predicate.getConditions().stream().forEach(cond -> {
					
						conditions.add(cond);
						conditionTypes.add(p.getType());
						
					});
					
				});
				
			}
			
		});
		
		if(sb.length() > 0) {
			appendType(sb, conditionTypes.get(0));
		}
		
		for(int i = 0; i < conditions.size(); i++) {
			if(i > 0) {
				appendType(sb, conditionTypes.get(i));
			}
			
			sb.append(conditions.get(i));
		}
	}

	private void appendType(StringBuilder sb, Type t) {
		if(Type.AND == t || Type.SPEC_AND == t) {
			sb.append(" and ");
			
		} else if(Type.OR == t || Type.SPEC_OR == t) {
			sb.append(" or ");
			
		}
	}

}
