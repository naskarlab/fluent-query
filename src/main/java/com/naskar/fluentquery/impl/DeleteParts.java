package com.naskar.fluentquery.impl;

public class DeleteParts {
	
	private StringBuilder table;
	private StringBuilder where;
	
	public DeleteParts() {
		this.table = new StringBuilder("");
		this.where = new StringBuilder("");
	}
	
	public StringBuilder getTable() {
		return table;
	}

	public StringBuilder getWhere() {
		return where;
	}
	
	public boolean hasWhere() {
		return where.length() > 0;
	}

}
