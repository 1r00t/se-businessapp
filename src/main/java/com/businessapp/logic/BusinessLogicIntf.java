package com.businessapp.logic;

import java.util.Collection;

import com.businessapp.entity.EntityIntf;
import com.businessapp.model.Customer;
import com.businessapp.model.Reservation;


/**
 * Public interface of business logic instance. Reference to anonymous implementation
 * is injected into the associated ViewController's.
 * <p>
 * Main task is to manage entities of specific types an entity data store.
 */
public interface BusinessLogicIntf extends LogicIntf {

	/*
	 * TODO: javadocs
	 */
	Customer newIndividualCustomer();

	Customer newBusinessCustomer();

	Reservation newReservation( Customer c );

	Collection<Customer>findAllCustomers();

	Collection<Reservation>findAllReservations();

	void deleteCustomer( String id );

	void deleteReservation( String id );

	void save( EntityIntf e );

	int size( Class<? extends EntityIntf>eType );

	String nextId( Class<? extends EntityIntf>eType );

	EntityIntf findById( Class<? extends EntityIntf>eType, String id );

	void loadData();

}
