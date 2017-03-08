package com.naskar.fluentquery.impl;

public class AliasGroupByImpl implements GroupByImpl {
	
	private String alias;
	
	public AliasGroupByImpl(String alias) {
		this.alias = alias;
	}
	
	public String getAlias() {
		return alias;
	}
	
}
