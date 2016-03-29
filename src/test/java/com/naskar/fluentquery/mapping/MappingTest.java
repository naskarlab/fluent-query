package com.naskar.fluentquery.mapping;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.QueryBuilder;
import com.naskar.fluentquery.conventions.MappingConvention;
import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.domain.Customer;

public class MappingTest {
	
	@Test
	public void testMapping() {
		MappingConvention mc = new MappingConvention();
		mc.add(
			new Mapping<Customer>()
				.to(Customer.class, "TB_CUSTOMER")
					.map(i -> i.getId(), "CD_CUSTOMER")
					.map(i -> i.getName(), "DS_NAME"));
		
		String expected = 
			"select e0.CD_CUSTOMER, e0.DS_NAME "
			+ "from TB_CUSTOMER e0 "
			+ "where e0.CD_CUSTOMER = 1 and e0.DS_NAME like 'r%'";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getId()).eq(1L)
				.and(i -> i.getName()).like("r%")
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL(mc))
			;
		
		Assert.assertEquals(expected, actual);
	}

}
