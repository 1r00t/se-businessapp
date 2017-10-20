package com.businessapp.model;

import com.businessapp.entity.annotations.Entity;
import com.businessapp.entity.annotations.Property;


@Entity
public class IndividualCustomer extends Customer {

	@Property( label="Vorname" )
	private String firstName;


	/*
	 * Public constructor(s).
	 */
	public IndividualCustomer() {
		super();
		this.firstName = null;
	}
	public IndividualCustomer( String id, String created, String firstName, String lastName ) {
		super( id, created, lastName );
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}

}
