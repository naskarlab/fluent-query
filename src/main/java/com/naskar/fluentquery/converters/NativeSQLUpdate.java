package com.naskar.fluentquery.converters;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.naskar.fluentquery.Into;
import com.naskar.fluentquery.Value;
import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.HolderInt;
import com.naskar.fluentquery.impl.MethodRecordProxy;
import com.naskar.fluentquery.impl.Tuple;
import com.naskar.fluentquery.impl.TypeUtils;
import com.naskar.fluentquery.impl.UpdateConverter;
import com.naskar.fluentquery.impl.UpdateImpl;
import com.naskar.fluentquery.impl.UpdateParts;
import com.naskar.fluentquery.impl.ValueImpl;

public class NativeSQLUpdate implements UpdateConverter<NativeSQLResult> {
	
	private Convention convention;
	private NativeSQLWhereImpl nativeWhereImpl;
	
	private boolean withoutAlias;
	
	public NativeSQLUpdate(Convention convention) {
		this.convention = convention;
		this.nativeWhereImpl = new NativeSQLWhereImpl();
		this.nativeWhereImpl.setConvention(convention);
		this.withoutAlias = false;
	}
	
	public void setWithoutAlias(boolean withoutAlias) {
		this.withoutAlias = withoutAlias;
	}
	
	public NativeSQLUpdate() {
		this(new SimpleConvention());
	}
	
	public NativeSQLUpdate setConvention(Convention convention) {
		this.convention = convention;
		this.nativeWhereImpl.setConvention(convention);
		return this;
	}
		
	@Override
	public <T> NativeSQLResult convert(UpdateImpl<T> updateImpl) {
		
		NativeSQLResult result = new NativeSQLResult();
		
		UpdateParts parts = new UpdateParts();
		
		HolderInt level = new HolderInt();
		level.value = 0;
		
		convert(updateImpl, parts, level, result, null);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("update ");
		sb.append(parts.getTable());
		
		sb.append(" set ");
		sb.append(parts.getSet());
		
		if(parts.hasWhere()) {
			sb.append(" where ");
			sb.append(parts.getWhere());
		}
		
		return result.sql(sb.toString());
	}
	
	private <T> void convert(UpdateImpl<T> updateImpl, UpdateParts parts, 
			final HolderInt level, NativeSQLResult result, List<String> parents) {
		MethodRecordProxy<T> proxy = TypeUtils.createProxy(updateImpl.getClazz());
		
		String alias = withoutAlias ? "" : "e" + level + ".";
		
		convertTable(parts.getTable(), alias, updateImpl.getClazz());
		convertSet(parts.getSet(), alias, proxy, updateImpl.getValues(), result);
		
		nativeWhereImpl.convertWhere(parts.getWhere(), alias, proxy, parents, updateImpl.getPredicates(), result);		
	}
	
	private <T> void convertTable(StringBuilder sb, String alias, Class<T> clazz) {
		String sufix = withoutAlias ? "" : " " + alias.substring(0, alias.length()-1);
		sb.append(convention.getNameFromClass(clazz) + sufix);
	}
	
	private <T> void convertSet(
		StringBuilder sb, 
		String alias,
		MethodRecordProxy<T> proxy,
		List<Tuple<Function<T, ?>, Value<Into<T>, ?>>> values,
		NativeSQLResult result
	) {
		
		String s = values.stream().map(i -> {
			
			@SuppressWarnings("unchecked")
			ValueImpl<Into<?>, ?> valueImpl = ((ValueImpl<Into<?>, ?>)(Object)i.getT2());
			Object value = valueImpl.get();
			
			proxy.clear();
			i.getT1().apply(proxy.getProxy());
			
			String name = 
				alias +	convention.getNameFromMethod(proxy.getMethods());
			
			return new StringBuilder(name)
						.append(" = :")
						.append(result.add(value))
						.toString();
						
		}).collect(Collectors.joining(", "));
		
		if(sb.length() > 0) {
			sb.append(", ");
		}
		
		sb.append(s);
	}
	
}
