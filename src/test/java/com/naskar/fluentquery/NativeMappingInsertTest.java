package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.naskar.fluentquery.conventions.MappingConvention;
import com.naskar.fluentquery.converters.NativeSQLInsertInto;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Customer;
import com.naskar.fluentquery.mapping.MappingFactory;

public class NativeMappingInsertTest {
	
	private MappingConvention mc;
	
	@Before
	public void setup() {
		mc = new MappingFactory().create();
	}
	
	@Test
	public void testInsert() {
		String expected = "insert into TB_CUSTOMER (CD_CUSTOMER, DS_NAME, VL_MIN_BALANCE) values (:p0, :p1, :p2)";
		
		NativeSQLResult result = new InsertBuilder()
			.into(Customer.class)
				.value(i -> i.getId()).set(1L)
				.value(i -> i.getName()).set("teste")
				.value(i -> i.getMinBalance()).set(10.2)
			.to(new NativeSQLInsertInto(mc));
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "teste");
		Assert.assertEquals(result.params().get("p2"), 10.2);
	}

}
