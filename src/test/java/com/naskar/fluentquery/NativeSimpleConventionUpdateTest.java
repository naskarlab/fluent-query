package com.naskar.fluentquery;
//package com.naskar.fluentquery;
//
//import java.util.Date;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.naskar.fluentquery.converters.NativeSQL;
//import com.naskar.fluentquery.domain.Account;
//import com.naskar.fluentquery.domain.Customer;
//
//public class TestNativeSimpleConventionUpdateTest {
//	
//	@Test
//	public void testUpdate() {
//		String expected = "update Customer set name = 'teste', date = '' where id = 1";
//		
//		String actual = new UpdateBuilder()
//			.entity(Customer.class)
//				.set(i -> i.getName(), "teste")
//				.set(i -> i.getDate(), new Date())
//			.where(i -> i.getId()).eq(1L)
//			.to(new NativeSQL());
//		
//		Assert.assertEquals(expected, actual);
//	}
//	
//}
