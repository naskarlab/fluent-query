package com.naskar.fluentquery.impl;

public class InsertParts {
	
	private StringBuilder into;
	private StringBuilder columns;
	private StringBuilder values;
	
	public InsertParts() {
		this.into = new StringBuilder("");
		this.columns = new StringBuilder("");
		this.values = new StringBuilder("");
	}

	public StringBuilder getInto() {
		return into;
	}

	public StringBuilder getColumns() {
		return columns;
	}

	public StringBuilder getValues() {
		return values;
	}
	
}
