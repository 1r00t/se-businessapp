package com.businessapp.model;

import com.businessapp.entity.annotations.Entity;
import com.businessapp.entity.annotations.Property;


@Entity
public class BusinessCustomer extends Customer {

	@Property( label="Steuer-Nr." )
	private String taxId;


	/*
	 * Public constructor(s).
	 */
	public BusinessCustomer() {
		super();
		this.taxId = null;
	}
	public BusinessCustomer( String id, String created, String companyName, String taxId ) {
		super( id, created, companyName );
		this.taxId = taxId;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId( String taxId ) {
		this.taxId = taxId;
	}

}
