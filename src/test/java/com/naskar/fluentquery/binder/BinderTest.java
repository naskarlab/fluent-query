package com.naskar.fluentquery.binder;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.naskar.fluentquery.InsertBuilder;
import com.naskar.fluentquery.QueryBuilder;
import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.converters.NativeSQLInsertInto;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.domain.Customer;
import com.naskar.fluentquery.model.CustomerFilterModel;

public class BinderTest {
	
	private NativeSQL nativeSQL;
	private BinderSQLBuilder binderBuilder;
	
	@Before
	public void setup() {
		this.nativeSQL = new NativeSQL();
		this.binderBuilder = new BinderSQLBuilder();
	}
	
	@Test
	public void testBinderQuery() {
		String expected = 
			"select e0.id, e0.name "
			+ "from Customer e0 "
			+ "where e0.name like :p0 and e0.regionCode = :p1"
			+ " and e0.regionCode > :p2 and e0.minBalance > :p3";
		
		BinderSQL<CustomerFilterModel> binder = binderBuilder
				.from(CustomerFilterModel.class);
					
		binder.configure(new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getName()).like(binder.get(i -> i.getName()))
				.and(i -> i.getRegionCode()).eq(binder.get(i -> i.getRegionCode()))
				.and(i -> i.getRegionCode()).gt(4L)
				.and(i -> i.getMinBalance()).gt(2.0)
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(nativeSQL)
		);
		
		CustomerFilterModel model = new CustomerFilterModel();
		model.setName("r%");
		model.setRegionCode(10L);
		
		NativeSQLResult result = binder.bind(model);
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get("p0"), model.getName());
		Assert.assertEquals(result.params().get("p1"), model.getRegionCode());
		Assert.assertEquals(result.params().get("p2"), 4L);
		Assert.assertEquals(result.params().get("p3"), 2.0);
	}
	
	@Test
	public void testInsert() {
		String expected = "insert into Customer"
				+ " (id, name, regionCode, minBalance, created)"
				+ " values (:p0, :p1, :p2, :p3, :p4)";
		
		BinderSQL<Customer> binder = binderBuilder
				.from(Customer.class);
		
		binder.configure(new InsertBuilder()
			.into(Customer.class)
				.value(i -> i.getId()).set(binder.get(i -> i.getId()))
				.value(i -> i.getName()).set(binder.get(i -> i.getName()))
				.value(i -> i.getRegionCode()).set(binder.get(i -> i.getRegionCode()))
				.value(i -> i.getMinBalance()).set(binder.get(i -> i.getMinBalance()))
				.value(i -> i.getCreated()).set(binder.get(i -> i.getCreated()))
			.to(new NativeSQLInsertInto())
		);
		
		Customer customer = new Customer();
		customer.setId(10L);
		customer.setName("rafael");
		customer.setRegionCode(20L);
		customer.setMinBalance(20.0);
		customer.setCreated(new Date());
		
		NativeSQLResult result = binder.bind(customer);
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		
		Assert.assertEquals(result.params().get("p0"), customer.getId());
		Assert.assertEquals(result.params().get("p1"), customer.getName());
		Assert.assertEquals(result.params().get("p2"), customer.getRegionCode());
		Assert.assertEquals(result.params().get("p3"), customer.getMinBalance());
		Assert.assertEquals(result.params().get("p4"), customer.getCreated());
	}
	
	@Test
	public void testMultipleInsert() {
		String expected = "insert into Customer (name) values (:p0)";
		
		BinderSQL<Customer> binder = binderBuilder
				.from(Customer.class);
		
		binder.configure(new InsertBuilder()
			.into(Customer.class)
				.value(i -> i.getName()).set(binder.get(i -> i.getName()))
			.to(new NativeSQLInsertInto())
		);
		
		Customer customer1 = new Customer();
		customer1.setName("teste");
		
		Customer customer2 = new Customer();
		customer2.setName("rafael");
		
		NativeSQLResult result1 = binder.bind(customer1);
		NativeSQLResult result2 = binder.bind(customer2);
		
		String actual1 = result1.sql();
		String actual2 = result2.sql();
		
		Assert.assertEquals(expected, actual1);
		Assert.assertEquals(expected, actual2);
		Assert.assertEquals(result1.params().get("p0"), customer1.getName());
		Assert.assertEquals(result2.params().get("p0"), customer2.getName());
	}

}
