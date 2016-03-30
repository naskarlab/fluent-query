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
 
```

## Using

```
<dependencies>
	<dependency>
		<groupId>com.naskar</groupId>
		<artifactId>fluent-query</artifactId>
		<version>0.0.1</version>
	</dependency>
</dependencies>
<repositories>
	<repository>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
		<id>central</id>
		<name>libs-release</name>
		<url>http://repo.naskar.com.br/dist/libs-release</url>
	</repository>
</repositories> 
```

