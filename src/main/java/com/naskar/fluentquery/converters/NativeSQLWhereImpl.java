package com.naskar.fluentquery.converters;

import java.util.ArrayList;
import java.util.List;

import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.PredicateImpl;
import com.naskar.fluentquery.impl.PredicateImpl.Type;

public class NativeSQLWhereImpl {
	
	private Convention convention;
	
	public void setConvention(Convention convention) {
		this.convention = convention;
	}
	
	public <T, I, B> void convertWhere(
			StringBuilder sb, 
			String alias,
			MethodRecordProxy<T> proxy,
			List<String> parents,
			List<PredicateImpl<T, Object, I, B>> predicates,
			NativeSQLResult result) {
		
		List<StringBuilder> conditions = new ArrayList<StringBuilder>();
		List<Type> conditionTypes = new ArrayList<Type>();
		
		predicates.stream().forEach(p -> {
			
			if(p.getType() == Type.SPEC_AND || p.getType() == Type.SPEC_OR) {
				
				StringBuilder sbSpec = new StringBuilder("");
				
				@SuppressWarnings("unchecked")
				PredicateProvider<T, B> q = ((PredicateProvider<T, B>)p.getProperty().apply(null));
				
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
					
					NativeSQLPredicate<T, Object, I> predicate = new NativeSQLPredicate<T, Object, I>(name, result);
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
