package com.businessapp.model;

import java.util.Date;

import com.businessapp.entity.EntityIntf;
import com.businessapp.entity.PropertyTypes;
import com.businessapp.entity.annotations.Entity;
import com.businessapp.entity.annotations.ID;
import com.businessapp.entity.annotations.Property;
import com.businessapp.logic.LogicFactory;


@Entity
public abstract class Customer implements EntityIntf {

	@ID
	@Property( label="Kunden-Nr." )
	private String id;

	@Property( label="Kunde seit", editable=false, dateFormat=Property.DateFormats.date )
	private Date created;

	@Property( label="Name" )
	private String name;


	/*
	 * Public constructor(s).
	 */
	public Customer() {
		this.id = LogicFactory.getBusinessLogic().nextId( Customer.class );
		this.created = new Date();
		this.name = null;
	}
	public Customer( String id, String created, String name ) {
		this.id = id;
		this.created = (Date)PropertyTypes.castFromString( created, Date.class, PropertyTypes.df_date );
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated( Date created ) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}


	/*
	 * Only place to create a new reservation.
	 * /
	public Reservation newReservation( String text, String from, String to ) {
		//GenericEntityLogicIntf<Reservation> logicImpl = LogicFactory.getReservationLogicImpl();
		BusinessLogicIntf bl = LogicFactory.getBusinessLogic();
		DataStore<Reservation> rsds = bl.getReservationDataStore();
		//String created = Property.df_date.format( new Date() );
		String created = PropertyTypes.castToString( new Date(), PropertyTypes.df_date );
		Reservation r = new Reservation(
			bl.getReservationDataStore().nextId(), this, text, from, to, created
		);
		rsds.save( r );
		bl.refreshView();
		return r;
	}
*/
}
