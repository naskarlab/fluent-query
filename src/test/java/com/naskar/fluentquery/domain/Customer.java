package com.naskar.fluentquery.domain;

import java.util.Date;

public class Customer {
	
	private Long id;
	private String name;
	private Long regionCode;
	private Double minBalance;
	private Date created;
	private Address mainAddress;
	private Address secondaryAddress;
	private Customer holder;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getRegionCode() {
		return regionCode;
	}
	
	public void setRegionCode(Long regionCode) {
		this.regionCode = regionCode;
	}
	
	public Double getMinBalance() {
		return minBalance;
	}
	
	public void setMinBalance(Double minBalance) {
		this.minBalance = minBalance;
	}

	public Address getMainAddress() {
		return mainAddress;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	public void setMainAddress(Address mainAddress) {
		this.mainAddress = mainAddress;
	}

	public Address getSecondaryAddress() {
		return secondaryAddress;
	}

	public void setSecondaryAddress(Address secondaryAddress) {
		this.secondaryAddress = secondaryAddress;
	}

	public Customer getHolder() {
		return holder;
	}

	public void setHolder(Customer holder) {
		this.holder = holder;
	}
	
}
