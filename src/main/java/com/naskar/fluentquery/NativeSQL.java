package com.naskar.fluentquery;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.naskar.fluentquery.impl.ConventionNamesUtils;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.NativeSQLPredicate;
import com.naskar.fluentquery.impl.PredicateImpl;

public class NativeSQL implements Converter<String> {

	@Override
	public <T> String convert(Class<T> clazz, List<Function<T, ?>> selects, List<PredicateImpl<T, Object>> predicates) {
		MethodRecordProxy<T> proxy = new MethodRecordProxy<T>(createInstance(clazz));
		
		StringBuilder sb = new StringBuilder("");
		
		sb.append("select ");
		sb.append(convertSelect(proxy, selects));
		sb.append(" from ");
		sb.append(convertClass(clazz));
		sb.append(" where ");
		sb.append(convertPredicates(proxy, predicates));
		
		return sb.toString();
	}
	
	private <T> T createInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> String convertSelect(MethodRecordProxy<T> proxy, List<Function<T, ?>> selects) {
		String s = selects.stream().map(i -> {
			
			i.apply(proxy.getProxy());
			
			return ConventionNamesUtils.getNameFromMethod(proxy.getCalledMethod());
			
		}).collect(Collectors.joining(", "));
		
		return s.isEmpty() ? " * " : s;
	}
	
	private <T> String convertClass(Class<T> clazz) {
		return ConventionNamesUtils.getNameFromClass(clazz);
	}
	
	private <T> String convertPredicates(MethodRecordProxy<T> proxy, List<PredicateImpl<T, Object>> predicates) {
		
		List<StringBuilder> conditions = new ArrayList<StringBuilder>();
		
		predicates.stream().forEach(p -> {
			
			p.getProperty().apply(proxy.getProxy());
			
			String name = 
				ConventionNamesUtils.getNameFromMethod(proxy.getCalledMethod());
			
			p.getActions().forEach(action -> {
				
				NativeSQLPredicate<T, Object> predicate = new NativeSQLPredicate<T, Object>(name);
				action.accept(predicate);
				
				predicate.getConditions().stream().forEach(sb -> {
				
					// TODO: AND
					conditions.add(sb);
					
				});
				
			});
			
		});
		
		return conditions.stream().map(i -> i.toString()).collect(Collectors.joining(" and "));
	}

}
