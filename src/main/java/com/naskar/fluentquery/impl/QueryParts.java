package com.naskar.fluentquery.impl;

public class QueryParts {
	
	private StringBuilder select;
	private StringBuilder from;
	private StringBuilder where;
	private StringBuilder groupBy;
	private StringBuilder orderBy;
	
	public QueryParts() {
		this.select = new StringBuilder("");
		this.from = new StringBuilder("");
		this.where = new StringBuilder("");
		this.groupBy = new StringBuilder("");
		this.orderBy = new StringBuilder("");
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
	
	public StringBuilder getGroupBy() {
		return groupBy;
	}
	
	public StringBuilder getOrderBy() {
		return orderBy;
	}
	
	public boolean hasWhere() {
		return where.length() > 0;
	}
	
	public boolean hasGroupBy() {
		return groupBy.length() > 0;
	}
	
	public boolean hasOrderBy() {
		return orderBy.length() > 0;
	}

}
