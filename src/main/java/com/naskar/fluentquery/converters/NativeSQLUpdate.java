//package com.naskar.fluentquery.converters;
//
//import java.lang.reflect.Method;
//import java.util.List;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import com.naskar.fluentquery.Into;
//import com.naskar.fluentquery.Value;
//import com.naskar.fluentquery.conventions.SimpleConvention;
//import com.naskar.fluentquery.impl.Convention;
//import com.naskar.fluentquery.impl.HolderInt;
//import com.naskar.fluentquery.impl.UpdateParts;
//import com.naskar.fluentquery.impl.MethodRecordProxy;
//import com.naskar.fluentquery.impl.Tuple;
//import com.naskar.fluentquery.impl.UpdateConverter;
//import com.naskar.fluentquery.impl.UpdateImpl;
//import com.naskar.fluentquery.impl.UpdateParts;
//import com.naskar.fluentquery.impl.ValueImpl;
//
//public class NativeSQLUpdate implements UpdateConverter<NativeSQLResult> {
//	
//	private Convention convention;
//	private NativeSQLWhereImpl whereImpl;
//	
//	public NativeSQLUpdate(Convention convention) {
//		this.convention = convention;
//		this.whereImpl = new NativeSQLWhereImpl();
//		this.whereImpl.setConvention(convention);
//	}
//	
//	public NativeSQLUpdate() {
//		this(new SimpleConvention());
//	}
//	
//	public NativeSQLUpdate setConvention(Convention convention) {
//		this.convention = convention;
//		this.whereImpl.setConvention(convention);
//		return this;
//	}
//		
//	@Override
//	public <T> NativeSQLResult convert(UpdateImpl<T> updateImpl) {
//		
//		NativeSQLResult result = new NativeSQLResult();
//		
//		UpdateParts parts = new UpdateParts();
//		
//		HolderInt level = new HolderInt();
//		level.value = 0;
//		
//		convert(updateImpl, parts, level, result, null);
//		
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("update ");
//		sb.append(parts.getTable());
//		
//		sb.append(parts.getSet());
//		
//		if(parts.hasWhere()) {
//			sb.append(" where ");
//			sb.append(parts.getWhere());
//		}
//		
//		return result.sql(sb.toString());
//	}
//	
//	private <T> void convert(UpdateImpl<T> updateImpl, UpdateParts parts, 
//			final HolderInt level, NativeSQLResult result, List<String> parents) {
//		MethodRecordProxy<T> proxy = new MethodRecordProxy<T>(createInstance(updateImpl.getClazz()));
//		
//		String alias = "e" + level + ".";
//		
//		convertTable(parts.getTable(), updateImpl.getClazz());
//		convertSet(parts.getSet(), updateImpl.getClazz());
//		
//		whereImpl.convertWhere(parts.getWhere(), alias, proxy, parents, updateImpl.getPredicates(), result);		
//	}
//	
//	private <T> T createInstance(Class<T> clazz) {
//		try {
//			return clazz.newInstance();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	private <T> void convertTable(StringBuilder sb, Class<T> clazz) {
//		sb.append(convention.getNameFromClass(clazz));
//	}
//	
//	private <T> void convertColumns(
//		StringBuilder sb, 
//		MethodRecordProxy<T> proxy,
//		List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values
//	) {
//		
//		String s = values.stream().map(i -> {
//			
//			proxy.clear();
//			i.getT1().apply(proxy.getProxy());
//			
//			Method m = proxy.getCalledMethod();
//			
//			return convention.getNameFromMethod(m);
//			
//		}).collect(Collectors.joining(", ", "(", ")"));
//		
//		if(sb.length() > 0) {
//			sb.append(", ");
//		}
//		
//		sb.append(s);
//	}
//	
//	private <T> void convertValues(
//		StringBuilder sb, 
//		MethodRecordProxy<T> proxy,
//		List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values,
//		NativeSQLResult result
//	) {
//		
//		String s = values.stream().map(i -> {
//			
//			@SuppressWarnings("unchecked")
//			ValueImpl<Into<?>, ?> valueImpl = ((ValueImpl<Into<?>, ?>)(Object)i.getT2());
//			Object value = valueImpl.get();
//			
//			return new StringBuilder(":").append(result.add(value)).toString();
//						
//		}).collect(Collectors.joining(", ", "(", ")"));
//		
//		if(sb.length() > 0) {
//			sb.append(", ");
//		}
//		
//		sb.append(s);
//	}
//	
//}
