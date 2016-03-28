package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.domain.Customer;

public class TestQuery {

	@Test
	public void test() {
		String expected = "select id, name from Customer where id = 1 and name like 'r%'";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getId()).eq(1L)
				.and(i -> i.getName()).like("r%")
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL())
			;
		
		Assert.assertEquals(expected, actual);
	}
	
}
