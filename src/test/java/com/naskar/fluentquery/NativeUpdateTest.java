package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.converters.NativeSQLUpdate;
import com.naskar.fluentquery.domain.Customer;

public class NativeUpdateTest {
	
	@Test
	public void testUpdate() {
		String expected = "update Customer e0 set e0.name = :p0, e0.minBalance = :p1 where e0.id = :p2";
		
		NativeSQLResult result = new UpdateBuilder()
			.entity(Customer.class)
				.value(i -> i.getName()).set("teste")
				.value(i -> i.getMinBalance()).set(10.2)
			.where(i -> i.getId()).eq(1L)
			.to(new NativeSQLUpdate().setWithoutAlias(false));
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), "teste");
		Assert.assertEquals(result.params().get("p1"), 10.2);
		Assert.assertEquals(result.params().get("p2"), 1L);
	}
	
	@Test
	public void testUpdateWithoutWhere() {
		String expected = "update Customer e0 set e0.name = :p0, e0.minBalance = :p1";
		
		NativeSQLResult result = new UpdateBuilder()
			.entity(Customer.class)
				.value(i -> i.getName()).set("teste")
				.value(i -> i.getMinBalance()).set(10.2)
			.to(new NativeSQLUpdate().setWithoutAlias(false));
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), "teste");
		Assert.assertEquals(result.params().get("p1"), 10.2);
	}
	
}
