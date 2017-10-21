package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.naskar.fluentquery.conventions.MappingConvention;
import com.naskar.fluentquery.converters.NativeSQLDelete;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Customer;
import com.naskar.fluentquery.mapping.MappingFactory;

public class NativeMappingDeleteTest {
	
	private MappingConvention mc;
	
	@Before
	public void setup() {
		mc = new MappingFactory().create();
	}
	
	@Test
	public void testDelete() {
		String expected = "delete from TB_CUSTOMER e0 where e0.CD_CUSTOMER = :p0";
		
		NativeSQLResult result = new DeleteBuilder()
			.entity(Customer.class)
			.where(i -> i.getId()).eq(1L)
			.to(new NativeSQLDelete(mc));
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), 1L);
	}
	
	@Test
	public void testDeleteWithoutWhere() {
		String expected = "delete from TB_CUSTOMER e0";
		
		NativeSQLResult result = new DeleteBuilder()
			.entity(Customer.class)
			.to(new NativeSQLDelete(mc));
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
	}
	
}
