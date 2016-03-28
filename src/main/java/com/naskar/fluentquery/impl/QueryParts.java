package com.naskar.fluentquery.impl;

public class QueryParts {
	
	private StringBuilder select;
	private StringBuilder from;
	private StringBuilder where;
	
	public QueryParts() {
		this.select = new StringBuilder("");
		this.from = new StringBuilder("");
		this.where = new StringBuilder("");
	}

	public StringBuilder getSelect() {
		return select;
	}

	public StringBuilder getFrom() {
		return from;
	}

	public StringBuilder getWhere() {
		return where;
	}
	
	public boolean hasWhere() {
		return where.length() > 0;
	}

}
