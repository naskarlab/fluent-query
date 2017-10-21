package com.naskar.fluentquery.impl;

import com.naskar.fluentquery.OrderBy;

public interface OrderByImpl<I> extends OrderBy<I> {
	
	public enum OrderByType { ASC, DESC }
	
	OrderByType getType();
}
