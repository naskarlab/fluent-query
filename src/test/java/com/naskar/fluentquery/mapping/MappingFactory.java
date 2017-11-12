package com.naskar.fluentquery.mapping;

import com.naskar.fluentquery.conventions.MappingConvention;
import com.naskar.fluentquery.domain.Account;
import com.naskar.fluentquery.domain.Address;
import com.naskar.fluentquery.domain.Customer;

public class MappingFactory {
	
	public MappingConvention create() {
		MappingConvention mc = new MappingConvention();
		
		mc.add(
			new MappingValueProvider<Customer>()
				.to(Customer.class, "TB_CUSTOMER")
					.map(i -> i.getId(), "CD_CUSTOMER", (i, v) -> i.setId(v))
					.map(i -> i.getName(), "DS_NAME", (i, v) -> i.setName(v))
					.map(i -> i.getMinBalance(), "VL_MIN_BALANCE", (i, v) -> i.setMinBalance(v))
					.map(i -> i.getRegionCode(), "NU_REGION_CODE", (i, v) -> i.setRegionCode(v))
					.map(i -> i.getMainAddress().getId(), "CD_ADDRESS_MAIN", (i, v) -> {
						i.setMainAddress(new Address());
						i.getMainAddress().setId(v);
					})
					.map(i -> i.getSecondaryAddress().getId(), "CD_ADDRESS_SECON", (i, v) -> {
						i.setSecondaryAddress(new Address());
						i.getSecondaryAddress().setId(v);
					})
					.map(i -> i.getHolder().getId(), "CD_HOLDER", (i, v) ->  {
						i.setHolder(new Customer());
						i.getHolder().setId(v);
					})
		);
		
		mc.add(
			new MappingValueProvider<Account>()
				.to(Account.class, "TB_ACCOUNT")
					.map(i -> i.getId(), "CD_ACCOUNT", (i, v) -> i.setId(v))
					.map(i -> i.getAccountNumber(), "NU_ACCOUNT", (i, v) -> i.setAccountNumber(v))
					.map(i -> i.getBalance(), "VL_BALANCE", (i, v) -> i.setBalance(v))
					.map(i -> i.getCustomer().getId(), "CD_CUSTOMER", (i, v) -> {
						i.setCustomer(new Customer());
						i.getCustomer().setId(v);
					})
					.map(i -> i.getCustomer().getRegionCode(), "NU_REGION_CODE", (i, v) -> i.getCustomer().setRegionCode(v))
		);
		
		mc.add(
			new MappingValueProvider<Address>()
				.to(Address.class, "TB_ADDRESS")
					.map(i -> i.getId(), "CD_ADDRESS", (i, v) -> i.setId(v))
					.map(i -> i.getDescription(), "DS_DESCRIPTION", (i, v) -> i.setDescription(v))
		);
		
		return mc;
	}	

}
