package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.naskar.fluentquery.conventions.MappingConvention;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.converters.NativeSQLUpdate;
import com.naskar.fluentquery.domain.Customer;
import com.naskar.fluentquery.mapping.MappingFactory;

public class NativeMappingUpdateTest {
	
	private MappingConvention mc;
	
	@Before
	public void setup() {
		mc = new MappingFactory().create();
	}
	
	@Test
	public void testUpdate() {
		String expected = "update TB_CUSTOMER e0 set e0.DS_NAME = :p0, e0.VL_MIN_BALANCE = :p1 where e0.CD_CUSTOMER = :p2";
		
		NativeSQLResult result = new UpdateBuilder()
			.entity(Customer.class)
				.value(i -> i.getName()).set("teste")
				.value(i -> i.getMinBalance()).set(10.2)
			.where(i -> i.getId()).eq(1L)
			.to(new NativeSQLUpdate(mc).setWithoutAlias(false));
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), "teste");
		Assert.assertEquals(result.params().get("p1"), 10.2);
		Assert.assertEquals(result.params().get("p2"), 1L);
	}
	
	@Test
	public void testUpdateWithoutWhere() {
		String expected = "update TB_CUSTOMER e0 set e0.DS_NAME = :p0, e0.VL_MIN_BALANCE = :p1";
		
		NativeSQLResult result = new UpdateBuilder()
			.entity(Customer.class)
				.value(i -> i.getName()).set("teste")
				.value(i -> i.getMinBalance()).set(10.2)
			.to(new NativeSQLUpdate(mc).setWithoutAlias(false));
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), "teste");
		Assert.assertEquals(result.params().get("p1"), 10.2);
	}
	
}
