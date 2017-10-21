package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.converters.NativeSQLUpdate;
import com.naskar.fluentquery.domain.Customer;

public class NativeUpdateTest {
	
	@Test
	public void testInsert() {
		String expected = "update Customer set name = :p0, minBalance = :p1 where id = :p2";
		
		NativeSQLResult result = new UpdateBuilder()
			.entity(Customer.class)
				.value(i -> i.getName()).set("teste")
				.value(i -> i.getMinBalance()).set(10.2)
			.where(i -> i.getId()).eq(1L)
			.to(new NativeSQLUpdate());
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "teste");
		Assert.assertEquals(result.params().get("p2"), 10.2);
	}
	
}
