package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.domain.Account;
import com.naskar.fluentquery.domain.Customer;

public class TestNativeSimpleConventionQueryTest {
	
	@Test
	public void testSelect() {
		String expected = "select e0.* from Customer e0";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.to(new NativeSQL())
			;
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCompleteLongString() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where e0.id = 1 and e0.name like 'r%'";
		
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
	
	@Test
	public void testTwoEntities() {
		String expected = 
			"select e0.name, e1.balance from Customer e0, Account e1" +
			" where e0.name like 'r%'" +
			" and e1.balance > 0.0" +
			" and e1.customer_id = e0.id" +
			" and e1.customer_regionCode = e0.regionCode" +
			" and e1.balance < e0.minBalance"
			;
		
		String actual = new QueryBuilder()
			.from(Customer.class)
				.where(i -> i.getName()).like("r%")
				.select(i -> i.getName())
			.from(Account.class, (query, parent) -> {
				
				query
					.where(i -> i.getBalance()).gt(0.0)
						.and(i -> i.getCustomer().getId()).eq(parent.getId())
						.and(i -> i.getCustomer().getRegionCode()).eq(parent.getRegionCode())
						.and(i -> i.getBalance()).lt(parent.getMinBalance())
					.select(i -> i.getBalance());
				
			})
			.to(new NativeSQL())
			;
		
		Assert.assertEquals(expected, actual);
	}
	
}
