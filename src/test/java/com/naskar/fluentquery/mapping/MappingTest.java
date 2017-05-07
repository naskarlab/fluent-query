package com.naskar.fluentquery.mapping;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.naskar.fluentquery.QueryBuilder;
import com.naskar.fluentquery.conventions.MappingConvention;
import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Account;
import com.naskar.fluentquery.domain.Address;
import com.naskar.fluentquery.domain.Customer;

public class MappingTest {
	
	private MappingConvention mc;
	
	@Before
	public void setup() {
		this.mc = new MappingConvention();
		
		mc.add(
			new Mapping<Customer>()
				.to(Customer.class, "TB_CUSTOMER")
					.map(i -> i.getId(), "CD_CUSTOMER")
					.map(i -> i.getName(), "DS_NAME")
					.map(i -> i.getMinBalance(), "VL_MIN_BALANCE")
					.map(i -> i.getRegionCode(), "NU_REGION_CODE")
					.map(i -> i.getMainAddress().getId(), "CD_ADDRESS_MAIN")
					.map(i -> i.getSecondaryAddress().getId(), "CD_ADDRESS_SECON")
					.map(i -> i.getHolder().getId(), "CD_HOLDER")
		);
		
		mc.add(
			new Mapping<Account>()
				.to(Account.class, "TB_ACCOUNT")
					.map(i -> i.getId(), "CD_ACCOUNT")
					.map(i -> i.getAccountNumber(), "NU_ACCOUNT")
					.map(i -> i.getBalance(), "VL_BALANCE")
					.map(i -> i.getCustomer().getId(), "CD_CUSTOMER")
					.map(i -> i.getCustomer().getRegionCode(), "NU_REGION_CODE")
		);
		
		mc.add(
			new Mapping<Address>()
				.to(Address.class, "TB_ADDRESS")
					.map(i -> i.getId(), "CD_ADDRESS")
					.map(i -> i.getDescription(), "DS_DESCRIPTION")
		);
	}
	
	@Test
	public void testSelect() {
		String expected = "select e0.* from TB_CUSTOMER e0";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.to(new NativeSQL(mc))
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testMapping() {
		String expected = 
			"select e0.CD_CUSTOMER, e0.DS_NAME "
			+ "from TB_CUSTOMER e0 "
			+ "where e0.CD_CUSTOMER = :p0 and e0.DS_NAME like :p1";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getId()).eq(1L)
				.and(i -> i.getName()).like("r%")
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL(mc))
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "r%");
	}
	
	@Test
	public void testMappingWithAlias() {
		String expected = 
			"select e0.CD_CUSTOMER as id, e0.DS_NAME as name "
			+ "from TB_CUSTOMER e0 "
			+ "where e0.CD_CUSTOMER = :p0 and e0.DS_NAME like :p1";
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getId()).eq(1L)
				.and(i -> i.getName()).like("r%")
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL(mc).setUsePropertyNameAsAlias(true))
			;
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), 1L);
		Assert.assertEquals(result.params().get("p1"), "r%");
	}
	
	@Test
	public void testTwoEntities() {
		String expected = 
			"select e0.DS_NAME, e1.VL_BALANCE from TB_CUSTOMER e0, TB_ACCOUNT e1" +
			" where e0.DS_NAME like :p0" +
			" and e1.VL_BALANCE > :p1" +
			" and e1.CD_CUSTOMER = e0.CD_CUSTOMER" +
			" and e1.NU_REGION_CODE = e0.NU_REGION_CODE" +
			" and e1.VL_BALANCE < e0.VL_MIN_BALANCE"
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
			.to(new NativeSQL(mc))
			;
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), "r%");
		Assert.assertEquals(result.params().get("p1"), 0.0);
	}
	
	@Test
	public void testTwoAttributesSameEntity() {
		String expected = 
			"select e0.DS_NAME, e1.DS_DESCRIPTION, e2.DS_DESCRIPTION" +
			" from TB_CUSTOMER e0, TB_ADDRESS e1, TB_ADDRESS e2" +
			" where e1.CD_ADDRESS = e0.CD_ADDRESS_MAIN" +
			" and e2.CD_ADDRESS = e0.CD_ADDRESS_SECON"
			;
		
		String actual = new QueryBuilder()
			.from(Customer.class)
				.select(i -> i.getName())
			.from(Address.class, (query, parent) -> {
				
				query
					.where(i -> i.getId()).eq(parent.getMainAddress().getId())
					.select(i -> i.getDescription());
				
			})
			.from(Address.class, (query, parent) -> {
				
				query
					.where(i -> i.getId()).eq(parent.getSecondaryAddress().getId())
					.select(i -> i.getDescription());
				
			})
			.to(new NativeSQL(mc))
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testAutoAssociation() {
		String expected = 
				"select e0.DS_NAME, e1.DS_NAME from TB_CUSTOMER e0, TB_CUSTOMER e1"
				+ " where e1.CD_CUSTOMER = e0.CD_HOLDER";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.from(Customer.class, (query, parent) -> {
				
				query
					.where(i -> i.getId()).eq(parent.getHolder().getId())
					.select(i -> i.getName())
					;
				
			})
			.select(i -> i.getName())
			.to(new NativeSQL(mc))
			.sql()
			;
		
		System.out.println(actual);
		
		Assert.assertEquals(expected, actual);
	}

}
