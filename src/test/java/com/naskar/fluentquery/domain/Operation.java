package com.naskar.fluentquery.domain;

public class Operation {

	private Long id;
	private Double amount;
	private Long accountId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return "Operation [id=" + id + ", amount=" + amount + ", accountId=" + accountId + "]";
	}

}
