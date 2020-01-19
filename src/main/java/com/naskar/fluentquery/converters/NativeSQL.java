package com.naskar.fluentquery.converters;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.naskar.fluentquery.Select;
import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.AliasGroupByImpl;
import com.naskar.fluentquery.impl.AliasOrderByImpl;
import com.naskar.fluentquery.impl.AttributeGroupByImpl;
import com.naskar.fluentquery.impl.AttributeOrderByImpl;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.GroupByImpl;
import com.naskar.fluentquery.impl.HolderInt;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.OrderByImpl;
import com.naskar.fluentquery.impl.QueryConverter;
import com.naskar.fluentquery.impl.QueryImpl;
import com.naskar.fluentquery.impl.QueryParts;
import com.naskar.fluentquery.impl.SelectImpl;
import com.naskar.fluentquery.impl.Tuple;
import com.naskar.fluentquery.impl.TypeUtils;

public class NativeSQL implements QueryConverter<NativeSQLResult> {
	
	private Convention convention;
	private Boolean usePropertyNameAsAlias;
	
	private NativeSQLWhereImpl nativeWhereImpl;
	
	public NativeSQL(Convention convention) {
		this.convention = convention;
		this.usePropertyNameAsAlias = false;
		this.nativeWhereImpl = new NativeSQLWhereImpl(this);
		this.nativeWhereImpl.setConvention(convention);
	}
	
	public NativeSQL() {
		this(new SimpleConvention());
	}
	
	public NativeSQL setConvention(Convention convention) {
		this.convention = convention;
		this.nativeWhereImpl.setConvention(convention);
		return this;
	}
	
	public NativeSQL setUsePropertyNameAsAlias(Boolean usePropertyNameAsAlias) {
		this.usePropertyNameAsAlias = usePropertyNameAsAlias;
		return this;
	}
	
	@Override
	public <T> NativeSQLResult convert(QueryImpl<T> queryImpl) {
		HolderInt level = new HolderInt();
		level.value = 0;
		
		return convert(queryImpl, null, level);
	}
	
	public <T> NativeSQLResult convert(QueryImpl<T> queryImpl, List<String> parentsTemp, HolderInt level) {
		
		NativeSQLResult result = new NativeSQLResult(level.value-1);
		
		QueryParts parts = new QueryParts();
		
		convert(queryImpl, parts, level, result, parentsTemp);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("select ");
		sb.append(parts.getSelect());
		
		sb.append(" from ");
		sb.append(parts.getFrom());
		
		if(parts.hasWhere()) {
			sb.append(" where ");
			sb.append(parts.getWhere());
		}
		
		if(parts.hasGroupBy()) {
			sb.append(" group by ");
			sb.append(parts.getGroupBy());
		}
		
		if(parts.hasOrderBy()) {
			sb.append(" order by ");
			sb.append(parts.getOrderBy());
		}
		
		if(queryImpl.getForUpdate()) {
			sb.append(" for update ");
		}
		
		return result.sql(sb.toString());
	}
	
	private <T> void convert(QueryImpl<T> queryImpl, QueryParts parts, final HolderInt level, NativeSQLResult result, List<String> parents) {
		MethodRecordProxy<T> proxy = TypeUtils.createProxy(queryImpl.getClazz());
		
		String alias = "e" + level + ".";
		
		if(!queryImpl.getWithoutSelect()) {
			convertSelect(parts.getSelect(), alias, proxy, queryImpl, 
				queryImpl.getSelects(), queryImpl.getSelectFunctions());
		}
		
		convertFrom(parts.getFrom(), alias, queryImpl.getClazz());
		
		nativeWhereImpl.convertWhere(parts.getWhere(), level, alias, proxy, parents, queryImpl.getPredicates(), result);
		
		convertGroupBy(parts.getGroupBy(), alias, proxy, queryImpl.getGroups());
		convertOrderBy(parts.getOrderBy(), alias, proxy, queryImpl.getOrders());
		convertFroms(parts, alias, level, result, proxy, queryImpl.getFroms());
	}

	private <T> void convertFroms(
			QueryParts parts, 
			String alias, 
			final HolderInt level,
			NativeSQLResult result, 
			MethodRecordProxy<T> proxy, 
			List<Tuple<QueryImpl<?>, Consumer<T>>> froms) {
		
		froms.forEach(i -> {
			
			proxy.clear();
			i.getT2().accept(proxy.getProxy());
			
			List<String> parentsTmp = createParents(alias, proxy);
			
			level.value++;
			convert(i.getT1(), parts, level, result, parentsTmp);
			
		});
	}

	public <T> List<String> createParents(String alias, MethodRecordProxy<T> proxy) {
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
		
		return parentsTmp;
	}
	
	private <T> void convertSelect(
		StringBuilder sb, String alias,
		MethodRecordProxy<T> proxy, 
		QueryImpl<T> queryImpl,
		List<Function<T, ?>> selects,
		Map<Function<T, ?>, Consumer<Select>> selectFunctions) {
		
		String s = selects.stream().map(i -> {
			
			proxy.clear();
			i.apply(proxy.getProxy());
			
			Method m = proxy.getCalledMethod();
			
			String name = convention.getNameFromMethod(m);
			String result = alias + name;
			
			result = executeSelectFunctions(result, name, selectFunctions, i, queryImpl);
			
			if(!(convention instanceof SimpleConvention) && usePropertyNameAsAlias != null && usePropertyNameAsAlias) {
				result = result + " as " + SimpleConvention.getPropertyNameFromMethod(m); 
			}
			
			return result;
			
		}).collect(Collectors.joining(", "));
		
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(s.isEmpty() ? alias + "*" : s);
	}

	private <T> String executeSelectFunctions(
			String column, 
			String name,
			Map<Function<T, ?>, Consumer<Select>> selectFunctions, 
			Function<T, ?> i,
			QueryImpl<T> queryImpl) {
		
		String result = column;
		
		Consumer<Select> actionSelect = selectFunctions.get(i); 
		if(actionSelect != null) {
			
			SelectImpl<T> selectImpl = new SelectImpl<T>(queryImpl, i);
			actionSelect.accept(selectImpl);
			
			Function<String, String> action = selectImpl.getAction();
			if(action != null) {
				String actionAlias =  selectImpl.getAlias();
				if(actionAlias == null) {
					actionAlias = name;
				}
				result = action.apply(result) + " as " + actionAlias;
			}
		}
		
		return result;
	}
	
	private <T> void convertGroupBy(
			StringBuilder sb, String alias,
			MethodRecordProxy<T> proxy, 
			List<GroupByImpl> groups) {
		
		if(!groups.isEmpty()) {
			String s = groups.stream().map(i -> {
				
				if(i instanceof AliasGroupByImpl) {
					
					AliasGroupByImpl group = (AliasGroupByImpl)i;
					
					return group.getAlias();
						
				} else if(i instanceof AttributeGroupByImpl) {
					
					@SuppressWarnings("unchecked")
					AttributeGroupByImpl<T, ?> group = (AttributeGroupByImpl<T, ?>)i;
					
					proxy.clear();
					group.getProperty().apply(proxy.getProxy());
					
					return 
						alias 
						+ convention.getNameFromMethod(proxy.getCalledMethod());
				} else {
					return "";
					
				}
				
			}).collect(Collectors.joining(", "));
					
			sb.append(s);
		}
	}
	
	private <T> void convertOrderBy(
		StringBuilder sb, String alias,
		MethodRecordProxy<T> proxy, 
		List<OrderByImpl<?>> orders) {
		
		if(!orders.isEmpty()) {
			String s = orders.stream().map(i -> {
				
				if(i instanceof AliasOrderByImpl) {
					
					AliasOrderByImpl<?> order = (AliasOrderByImpl<?>)i;
					
					return 
						order.getAlias()
						+ (OrderByImpl.OrderByType.DESC.equals(i.getType()) ? " desc" : "");
						
				} else if(i instanceof AttributeOrderByImpl) {
					
					@SuppressWarnings("unchecked")
					AttributeOrderByImpl<?, T, ?> order = (AttributeOrderByImpl<?, T, ?>)i;
					
					proxy.clear();
					order.getProperty().apply(proxy.getProxy());
					
					return 
						alias 
						+ convention.getNameFromMethod(proxy.getCalledMethod()) 
						+ (OrderByImpl.OrderByType.DESC.equals(i.getType()) ? " desc" : "");
				} else {
					return "";
					
				}
				
			}).collect(Collectors.joining(", "));
					
			sb.append(s);
		}
	}
	
	private <T> void convertFrom(
			StringBuilder sb, String alias, Class<T> clazz) {
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(convention.getNameFromClass(clazz) + " " + 
			alias.substring(0, alias.length()-1));
	}
	
}
