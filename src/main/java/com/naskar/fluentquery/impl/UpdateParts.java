package com.naskar.fluentquery.impl;

public class UpdateParts {
	
	private StringBuilder table;
	private StringBuilder set;
	private StringBuilder where;
	
	public UpdateParts() {
		this.table = new StringBuilder("");
		this.set = new StringBuilder("");
		this.where = new StringBuilder("");
	}
	
	public StringBuilder getTable() {
		return table;
	}

	public StringBuilder getSet() {
		return set;
	}

	public StringBuilder getWhere() {
		return where;
	}
	
	public boolean hasWhere() {
		return where.length() > 0;
	}

}
