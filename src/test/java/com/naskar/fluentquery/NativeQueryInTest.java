package com.naskar.fluentquery;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Account;
import com.naskar.fluentquery.domain.Operation;

public class NativeQueryInTest {
	
	@Test
	public void testSelect() {
		String expected = 
			"select e0.* from Account e0 where e0.id not in ("
			+ "select e1000.accountId from Operation e1000)";
		
		String actual = new QueryBuilder()
			.from(Account.class)
			.where(x -> x.getId()).notIn(Operation.class, (q, parent) -> {
				q.select(x -> x.getAccountId());
			})
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testSelectParent() {
		String expected = 
			"select e0.* from Account e0 where e0.id not in "
			+ "(select e1000.accountId from Operation e1000 where e1000.amount > e0.balance and e1000.amount > :p0)"
			+ " and e0.balance > :p1";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Account.class)
			.where(x -> x.getId()).notIn(Operation.class, (q, parent) -> {
				q.where(y -> y.getAmount()).gt(parent.getBalance())
					.and(y -> y.getAmount()).gt(1.6);
				q.select(y -> y.getAccountId());
			})
			.and(x -> x.getBalance()).gt(2.5)
			.to(new NativeSQL())
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.values(),  Arrays.asList(1.6, 2.5));
		Assert.assertEquals(result.params().get("p0"), 1.6);
		Assert.assertEquals(result.params().get("p1"), 2.5);
	}
	
}
