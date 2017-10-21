package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQLDelete;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Customer;

public class NativeDeleteTest {
	
	@Test
	public void testDelete() {
		String expected = "delete from Customer e0 where e0.id = :p0";
		
		NativeSQLResult result = new DeleteBuilder()
			.entity(Customer.class)
			.where(i -> i.getId()).eq(1L)
			.to(new NativeSQLDelete());
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), 1L);
	}
	
	@Test
	public void testDeleteWithoutWhere() {
		String expected = "delete from Customer e0";
		
		NativeSQLResult result = new DeleteBuilder()
			.entity(Customer.class)
			.to(new NativeSQLDelete());
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
	}
	
}
