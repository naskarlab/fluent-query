package com.naskar.fluentquery;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.domain.Account;
import com.naskar.fluentquery.domain.Customer;

public class TestNativeSimpleConventionInsertTest {
	
	/*
	@Test
	public void testInsert() {
		String expected = "insert into Customer (name, date) values ('teste', '')";
		
		String actual = new InsertBuilder()
			.into(Customer.class)
				.value(i -> i.getName(), "teste")
				.value(i -> i.getDate(), new Date())
			.to(new NativeSQL());
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testInsertFields() {
		String expected = "insert into Customer (name, date) values ('teste', '')";
		
		String actual = new InsertBuilder()
			.into(Customer.class)
				.value(i -> i.getName(), "teste")
				.value(i -> i.getDate(), new Date())
			.to(new NativeSQL());
		
		Assert.assertEquals(expected, actual);
	}
	*/
	
}
