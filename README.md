# Fluent Query

Create Queries, Inserts, Updates e Deletes using only POJO classes.  

## Features

* Configuration over code: independence business code of the infrastructure code;
* Intrusive-less: zero or less changes for your code;
* Glue code: it’s only a small and simple classes set;
* Fluent Builder: code complete is your friend!
* Check compiler: Refactory ? No problem. Do the compiler to work to you.

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

@Test
public void testInsert() {
	String expected = "insert into Customer (id, name, minBalance) values (:p0, :p1, :p2)";
	
	NativeSQLResult result = new InsertBuilder()
		.into(Customer.class)
			.value(i -> i.getId()).set(1L)
			.value(i -> i.getName()).set("teste")
			.value(i -> i.getMinBalance()).set(10.2)
		.to(new NativeSQLInsertInto());
	
	String actual = result.sql();
	
	Assert.assertEquals(expected, actual);
	
	Assert.assertEquals(result.params().get("p0"), 1L);
	Assert.assertEquals(result.params().get("p1"), "teste");
	Assert.assertEquals(result.params().get("p2"), 10.2);
}

@Test
public void testUpdate() {
	String expected = "update Customer e0 set e0.name = :p0, e0.minBalance = :p1 where e0.id = :p2";
	
	NativeSQLResult result = new UpdateBuilder()
		.entity(Customer.class)
			.value(i -> i.getName()).set("teste")
			.value(i -> i.getMinBalance()).set(10.2)
		.where(i -> i.getId()).eq(1L)
		.to(new NativeSQLUpdate());
	
	String actual = result.sql();
	
	Assert.assertEquals(expected, actual);
	Assert.assertEquals(result.params().get("p0"), "teste");
	Assert.assertEquals(result.params().get("p1"), 10.2);
	Assert.assertEquals(result.params().get("p2"), 1L);
}

@Test
public void testDelete() {
	String expected = "delete from Customer e0 where e0.id = :p0";
	
	NativeSQLResult result = new DeleteBuilder()
		.entity(Customer.class)
		.where(i -> i.getId()).eq(1L)
		.to(new NativeSQLDelete());
	
	String actual = result.sql();
	
	Assert.assertEquals(expected, actual);	
	Assert.assertEquals(result.params().get("p0"), 1L);
}
	 
```

## Usage Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.naskarlab</groupId>
    <artifactId>fluent-query</artifactId>
    <version>0.1.0</version>
</dependency>

```


## Releases

### 0.0.1 
	- Simple Queries

### 0.1.0 
	- Insert, Update and Delete included.
