package com.naskar.fluentquery.converters;

import java.util.List;

import com.naskar.fluentquery.conventions.SimpleConvention;
import com.naskar.fluentquery.impl.Convention;
import com.naskar.fluentquery.impl.DeleteConverter;
import com.naskar.fluentquery.impl.DeleteImpl;
import com.naskar.fluentquery.impl.DeleteParts;
import com.naskar.fluentquery.impl.HolderInt;
import com.naskar.fluentquery.impl.MethodRecordProxy;

public class NativeSQLDelete implements DeleteConverter<NativeSQLResult> {
	
	private Convention convention;
	private NativeSQLWhereImpl nativeWhereImpl;
	
	public NativeSQLDelete(Convention convention) {
		this.convention = convention;
		this.nativeWhereImpl = new NativeSQLWhereImpl();
		this.nativeWhereImpl.setConvention(convention);
	}
	
	public NativeSQLDelete() {
		this(new SimpleConvention());
	}
	
	public NativeSQLDelete setConvention(Convention convention) {
		this.convention = convention;
		this.nativeWhereImpl.setConvention(convention);
		return this;
	}
		
	@Override
	public <T> NativeSQLResult convert(DeleteImpl<T> deleteImpl) {
		
		NativeSQLResult result = new NativeSQLResult();
		
		DeleteParts parts = new DeleteParts();
		
		HolderInt level = new HolderInt();
		level.value = 0;
		
		convert(deleteImpl, parts, level, result, null);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("delete from ");
		sb.append(parts.getTable());
		
		if(parts.hasWhere()) {
			sb.append(" where ");
			sb.append(parts.getWhere());
		}
		
		return result.sql(sb.toString());
	}
	
	private <T> void convert(DeleteImpl<T> deleteImpl, DeleteParts parts, 
			final HolderInt level, NativeSQLResult result, List<String> parents) {
		MethodRecordProxy<T> proxy = new MethodRecordProxy<T>(createInstance(deleteImpl.getClazz()));
		
		String alias = "e" + level + ".";
		
		convertTable(parts.getTable(), alias, deleteImpl.getClazz());
		
		nativeWhereImpl.convertWhere(parts.getWhere(), alias, proxy, parents, deleteImpl.getPredicates(), result);		
	}
	
	private <T> T createInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private <T> void convertTable(StringBuilder sb, String alias, Class<T> clazz) {
		sb.append(convention.getNameFromClass(clazz) + " " + 
			alias.substring(0, alias.length()-1));
	}
	
}
