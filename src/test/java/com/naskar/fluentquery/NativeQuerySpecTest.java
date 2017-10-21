package com.naskar.fluentquery;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Customer;

public class NativeQuerySpecTest {
	
	@Test
	public void testSimpleSpec() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where (e0.regionCode = :p0)";
		
		Consumer<Query<Customer>> regionCode1 = 
			q -> q.where(i -> i.getRegionCode()).eq(1L);  
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.whereSpec(regionCode1)
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL())
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), 1L);
	}
	
}
