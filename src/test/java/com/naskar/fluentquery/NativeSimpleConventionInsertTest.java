package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQLInsertInto;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Customer;

public class NativeSimpleConventionInsertTest {
	
	@Test
	public void testInsert() {
		String expected = "insert into Customer (id, name, minBalance) values (:p0, :p1, :p2)";
		
		NativeSQLResult result = new InsertBuilder()
			.into(Customer.class)
				.value(i -> i.getId()).set(1L)
				.value(i -> i.getName()).set("teste")
				.value(i -> i.getMinBalance()).set(10.2)
			.to(new NativeSQLInsertInto());
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "teste");
		Assert.assertEquals(result.params().get("p2"), 10.2);
	}
	
}
