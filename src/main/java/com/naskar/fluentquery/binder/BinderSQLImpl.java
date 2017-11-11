package com.naskar.fluentquery.binder;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.impl.MethodRecordProxy;

public class BinderSQLImpl<T> implements BinderSQL<T> {
	
	private NativeSQLResult result;
	
	private Map<Object, Function<T, ?>> maps;
	private MethodRecordProxy<T> proxy;

	public BinderSQLImpl(Class<T> clazz) {
		this.maps = new IdentityHashMap<Object, Function<T, ?>>();
		this.proxy = new MethodRecordProxy<T>(clazz);
		this.proxy.setExecute(false);
	}

	@Override
	public void configure(NativeSQLResult result) {
		this.result = result;
	}

	@Override
	public <R> R get(Function<T, R> getter) {
		proxy.clear();
		getter.apply(proxy.getProxy());
		
		R r = createInstance(proxy.getCalledMethod().getReturnType());
		
		maps.put(r, getter);
		
		return r;
	}

	@SuppressWarnings("unchecked")
	private <R, E> R createInstance(Class<E> returnType) {
		if(Long.class.equals(returnType)) {
			return (R) new Long(0L);
			
		} else if(Double.class.equals(returnType)) {
				return (R) new Double(0L);
				
		} else {
			return (R)MethodRecordProxy.createInstance(returnType);
		}
	}

	@Override
	public NativeSQLResult bind(T t) {
		
		NativeSQLResult newResult = new NativeSQLResult();
		newResult.sql(result.sql());
		
		Map<String, Object> params = newResult.params();
		List<Object> values = newResult.values();
		
		for(Entry<String, Object> e : result.params().entrySet()) {
			
			Object v = e.getValue();
			
			Function<T, ?> f = maps.get(e.getValue());
			if(f != null) {
				v = f.apply(t);
				
			}
			
			params.put(e.getKey(), v);
			values.add(v);
		}
		
		return newResult;
	}

}
