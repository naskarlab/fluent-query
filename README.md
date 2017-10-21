# Fluent Query

Create Queries using only POJO classes.  

## Features

* Configuration over code: independence business code of the infrastructure code;
* Intrusive-less: zero or less changes for your code;
* Glue code: it’s only a small and simple classes set;
* Fluent Builder: code complete is your friend!


## Examples

```

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
 
```

## TODO

- Insert
- Update
- Delete
