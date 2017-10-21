package com.naskar.fluentquery.mapping;

import com.naskar.fluentquery.conventions.MappingConvention;
import com.naskar.fluentquery.domain.Account;
import com.naskar.fluentquery.domain.Address;
import com.naskar.fluentquery.domain.Customer;

public class MappingFactory {
	
	public MappingConvention create() {
		MappingConvention mc = new MappingConvention();
		
		mc.add(
			new Mapping<Customer>()
				.to(Customer.class, "TB_CUSTOMER")
					.map(i -> i.getId(), "CD_CUSTOMER")
					.map(i -> i.getName(), "DS_NAME")
					.map(i -> i.getMinBalance(), "VL_MIN_BALANCE")
					.map(i -> i.getRegionCode(), "NU_REGION_CODE")
					.map(i -> i.getMainAddress().getId(), "CD_ADDRESS_MAIN")
					.map(i -> i.getSecondaryAddress().getId(), "CD_ADDRESS_SECON")
					.map(i -> i.getHolder().getId(), "CD_HOLDER")
		);
		
		mc.add(
			new Mapping<Account>()
				.to(Account.class, "TB_ACCOUNT")
					.map(i -> i.getId(), "CD_ACCOUNT")
					.map(i -> i.getAccountNumber(), "NU_ACCOUNT")
					.map(i -> i.getBalance(), "VL_BALANCE")
					.map(i -> i.getCustomer().getId(), "CD_CUSTOMER")
					.map(i -> i.getCustomer().getRegionCode(), "NU_REGION_CODE")
		);
		
		mc.add(
			new Mapping<Address>()
				.to(Address.class, "TB_ADDRESS")
					.map(i -> i.getId(), "CD_ADDRESS")
					.map(i -> i.getDescription(), "DS_DESCRIPTION")
		);
		
		return mc;
	}	

}
