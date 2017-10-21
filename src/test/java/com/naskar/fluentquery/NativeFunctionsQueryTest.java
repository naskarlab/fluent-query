package com.naskar.fluentquery;

import org.junit.Assert;
import org.junit.Test;

import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.domain.Customer;

public class NativeFunctionsQueryTest {
	
	@Test
	public void testFuncToCharBy() {
		String expected = "select to_char(e0.regionCode,'999D99S') as descRegionCode from Customer e0";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.select(x -> x.getRegionCode(), s -> s.func(
					c -> "to_char(" + c + ",'999D99S')", "descRegionCode"))
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testFuncToCharOthersAttrsBy() {
		String expected = "select e0.id, e0.regionCode, "
			+ "to_char(e0.regionCode,'999D99S') as descRegionCode from Customer e0";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.select(x -> x.getId())
			.select(x -> x.getRegionCode())
			.select(x -> x.getRegionCode(), s -> s.func(
					c -> "to_char(" + c + ",'999D99S')", "descRegionCode"))
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCountGroupBy() {
		String expected = "select e0.regionCode, count(1) as c "
			+ "from Customer e0 group by e0.regionCode order by e0.regionCode";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.select(x -> x.getRegionCode(), s -> s.groupBy().orderBy().asc())
			.select(x -> x.getRegionCode(), 
				s -> s.func(c -> "count(1)", "c"))
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCountGroupByFunction() {
		String expected = "select to_char(e0.regionCode,'999D99S') as g, count(1) as c "
			+ "from Customer e0 group by g order by g";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.select(x -> x.getRegionCode(), s -> 
				s.func(c -> "to_char(" + c + ",'999D99S')", "g").groupBy().orderBy().asc())
			.select(x -> x.getRegionCode(), 
				s -> s.func(c -> "count(1)", "c"))
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSimpleGroupBy() {
		String expected = "select e0.regionCode from Customer e0 group by e0.regionCode";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.select(x -> x.getRegionCode())
			.groupBy(x -> x.getRegionCode())
			.to(new NativeSQL())
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}

}
