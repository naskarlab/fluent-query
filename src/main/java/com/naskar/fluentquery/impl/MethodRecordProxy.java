package com.naskar.fluentquery.impl;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MethodRecordProxy<T> implements MethodInterceptor {
	
	private T proxy;
	private Method method;
	private T target;
	
	@SuppressWarnings("unchecked")
	public MethodRecordProxy(T target) {
		this.target = target;
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(this);
		
		this.proxy = (T) enhancer.create();
	}
	
	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		this.method = method;
		return methodProxy.invoke(target, args);
	}
	
	public Method getCalledMethod() {
		return method;
	}
	
	public T getProxy() {
		return proxy;
	}
	
}
