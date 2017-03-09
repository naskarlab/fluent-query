package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Account;
import com.naskar.fluentquery.domain.Customer;

public class TestNativeSimpleConventionQueryTest {
	
	@Test
	public void testSelect() {
		String expected = "select e0.* from Customer e0";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOrderBy() {
		String expected = "select e0.* from Customer e0 order by e0.id, e0.name desc";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.orderBy(x -> x.getId()).asc()
			.orderBy(x -> x.getName()).desc()
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCompleteLongString() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where e0.id = :p0 and e0.name like :p1";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getId()).eq(1L)
				.and(i -> i.getName()).like("r%")
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL())
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "r%");
	}
	
	@Test
	public void testTwoEntities() {
		String expected = 
			"select e0.name, e1.balance from Customer e0, Account e1" +
			" where e0.name like :p0" +
			" and e1.balance > :p1" +
			" and e1.customer_id = e0.id" +
			" and e1.customer_regionCode = e0.regionCode" +
			" and e1.balance < e0.minBalance"
			;
		
		NativeSQLResult result = new QueryBuilder()
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
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), "r%");
		Assert.assertEquals(result.params().get("p1"), 0.0);
	}
	
	@Test
	public void testAndOr() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where (e0.name like :p0 or e0.name like :p1) and e0.id = :p2";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.whereSpec(e -> {
				e.where(i -> i.getName()).like("c%")
					.or(i -> i.getName()).like("r%");
			})
			.and(i -> i.getId()).eq(1L)
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL())
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), "c%");
		Assert.assertEquals(result.params().get("p1"), "r%");
		Assert.assertEquals(result.params().get("p2"), 1L);
	}
	
	@Test
	public void testComplexAndOr() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where e0.regionCode > :p0"
			+ " or (e0.regionCode = :p1 and e0.name like :p2)";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getRegionCode()).gt(3L)
			.orSpec(e -> {
				e.where(i -> i.getRegionCode()).eq(2L)
					.and(i -> i.getName()).like("r%");
			})
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL())
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), 3L);
		Assert.assertEquals(result.params().get("p1"), 2L);
		Assert.assertEquals(result.params().get("p2"), "r%");
	}
	
	@Test
	public void testComplexGroupAndOr() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where (e0.regionCode = :p0 and e0.name like :p1)"
			+ " or (e0.regionCode = :p2 and e0.name like :p3)";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.whereSpec(e -> {
				e.where(i -> i.getRegionCode()).eq(1L)
					.and(i -> i.getName()).like("c%");
			})
			.orSpec(e -> {
				e.where(i -> i.getRegionCode()).eq(2L)
					.and(i -> i.getName()).like("r%");
			})
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL())
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "c%");
		Assert.assertEquals(result.params().get("p2"), 2L);
		Assert.assertEquals(result.params().get("p3"), "r%");
	}
	
	@Test
	public void testComplexNestedGroupAndOr() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where (e0.regionCode = :p0 and (e0.name like :p1 or e0.name like :p2))"
			+ " or (e0.regionCode > :p3 and e0.name like :p4)";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.whereSpec(e -> {
				e.where(i -> i.getRegionCode()).eq(1L)
					.andSpec(q -> {
						q.where(i -> i.getName()).like("r%")
							.or(i -> i.getName()).like("c%");
						});
			})
			.orSpec(e -> {
				e.where(i -> i.getRegionCode()).gt(1L)
					.and(i -> i.getName()).like("d%");
			})
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL())
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "r%");
		Assert.assertEquals(result.params().get("p2"), "c%");
		Assert.assertEquals(result.params().get("p3"), 1L);
		Assert.assertEquals(result.params().get("p4"), "d%");
	}
	
}
