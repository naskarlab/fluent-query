package com.naskar.fluentquery.converters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Value;
import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.InsertParts;
import com.naskar.fluentquery.impl.IntoConverter;
import com.naskar.fluentquery.impl.IntoImpl;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.Tuple;
import com.naskar.fluentquery.impl.TypeUtils;
import com.naskar.fluentquery.impl.ValueImpl;

public class NativeSQLInsertInto implements IntoConverter<NativeSQLResult> {
	
	private Convention convention;
	
	public NativeSQLInsertInto(Convention convention) {
		this.convention = convention;
	}
	
	public NativeSQLInsertInto() {
		this(new SimpleConvention());
	}
	
	public NativeSQLInsertInto setConvention(Convention convention) {
		this.convention = convention;
		return this;
	}
		
	@Override
	public <T> NativeSQLResult convert(IntoImpl<T> intoImpl) {
		
		NativeSQLResult result = new NativeSQLResult();
		
		InsertParts parts = new InsertParts();
		
		convert(intoImpl, parts, result);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("insert into ");
		sb.append(parts.getInto());
		sb.append(" ");
		
		sb.append(parts.getColumns());
		
		sb.append(" values ");
		sb.append(parts.getValues());
		
		return result.sql(sb.toString());
	}
	
	private <T> void convert(IntoImpl<T> intoImpl, InsertParts parts, NativeSQLResult result) {
		MethodRecordProxy<T> proxy = TypeUtils.createProxy(intoImpl.getClazz());
		convertInto(parts.getInto(), intoImpl.getClazz());
		convertColumns(parts.getColumns(), proxy, intoImpl.getValues());
		convertValues(parts.getValues(), proxy, intoImpl.getValues(), result);		
	}
	
	private <T> void convertInto(StringBuilder sb, Class<T> clazz) {
		sb.append(convention.getNameFromClass(clazz));
	}
	
	private <T> void convertColumns(
		StringBuilder sb, 
		MethodRecordProxy<T> proxy,
		List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values
	) {
		
		String s = values.stream().map(i -> {
			
			proxy.clear();
			i.getT1().apply(proxy.getProxy());
			
			Method m = proxy.getCalledMethod();
			
			return convention.getNameFromMethod(m);
			
		}).collect(Collectors.joining(", ", "(", ")"));
		
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(s);
	}
	
	private <T> void convertValues(
		StringBuilder sb, 
		MethodRecordProxy<T> proxy,
		List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values,
		NativeSQLResult result
	) {
		
		String s = values.stream().map(i -> {
			
			@SuppressWarnings("unchecked")
			ValueImpl<Into<?>, ?> valueImpl = ((ValueImpl<Into<?>, ?>)(Object)i.getT2());
			Object value = valueImpl.get();
			
			return new StringBuilder(":").append(result.add(value)).toString();
						
		}).collect(Collectors.joining(", ", "(", ")"));
		
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(s);
	}
	
}
