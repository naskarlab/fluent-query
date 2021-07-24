# Fluent Query

Create Queries, Inserts, Updates e Deletes using only POJO classes.  

## Features

* Configuration over code: independence business code of the infrastructure code;
* Intrusive-less: zero or less changes for your code;
* Glue code: it’s only a small and simple classes set;
* Fluent Builder: code complete is your friend!
* Check compiler: Refactory ? No problem. Do the compiler to work to you.
* Performance-based: if you are paranoid by performance, use binder parameters to create Queries, Insert, Update and Deletes.

## Mapping

* JPA EclipseLink: [Fluent Query EclipseLink](https://github.com/naskarlab/fluent-query-eclipselink)
* JPA:             [Fluent Query JPA - Any Provider](https://github.com/naskarlab/fluent-query-jpa-metamodel)
* JDBC Drivers:    [Fluent Query JDBC](https://github.com/naskarlab/fluent-query-jdbc)
* MongoDB:         [Fluent Query MongoDB](https://github.com/naskarlab/fluent-query-mongodb)

## Kotlin Support

See examples in:

* [Fluent Query JPA Kotlin Examples](https://github.com/naskarlab/fluent-query-kotlin-example)
 


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


### Insert, Update and Delete

```
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


### Binder Parameters

```

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
	 
```

## Usage with Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
	<repository>
	    <id>gitlab-maven</id>
	    <url>https://gitlab.com/api/v4/projects/23719062/packages/maven</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.naskarlab</groupId>
    <artifactId>fluent-query</artifactId>
    <version>0.3.0</version>
</dependency>

```


## Releases

### 0.3.3
	Sync packages on jitpack and gitlab.

### 0.3.2
	Github actions for publish on gitlab packages maven repository
	[GitLab Packages](https://gitlab.com/naskarlab/maven-repository/-/packages/2311139)

### 0.3.1
	- Included IN/NOT IN
	- Included FOR UPDATE for SELECTs
	- Included JPA Metamodel support, ie, now it support any JPA provider, beyond the JDBC and MongoDB.

### 0.3.0 
	- MappingValueProvider included for fill entities using unstructured datas.

### 0.2.0 
	- Use Binder parameters to create caches for queries, inserts, updates and delete sqls.

### 0.1.0 
	- Insert, Update and Delete included.

### 0.0.1 
	- Simple Queries
