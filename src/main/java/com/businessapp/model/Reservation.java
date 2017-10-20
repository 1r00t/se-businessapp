package com.businessapp.model;

import java.util.Date;

import com.businessapp.entity.EntityIntf;
import com.businessapp.entity.PropertyTypes;
import com.businessapp.entity.annotations.Entity;
import com.businessapp.entity.annotations.ID;
import com.businessapp.entity.annotations.Property;


@Entity
public class Reservation implements EntityIntf {

	@ID
	@Property( label="Reserv.-Nr." )
	private String reservationId;

	@Property( label="Kunden-Nr.", editable=false )
	private Customer customer;

	@Property( label="Text" )
	private String text;

	@Property( label="von", dateFormat=Property.DateFormats.datetime )
	private Date from;

	@Property( label="bis", dateFormat=Property.DateFormats.datetime )
	private Date to;

	@Property( label="Erstellt am", dateFormat=Property.DateFormats.date, editable=false  )
	private Date created;


	/*
	 * Protected constructor.
	 * A reservation can only be constructed from a valid customer reference.
	 */
	public Reservation( String reservationId, Customer customer, String text, String from, String to, String created ) {
		this.reservationId = reservationId;
		this.customer = customer;
		this.text = text;
		this.from = (Date)PropertyTypes.castFromString( from, Date.class, PropertyTypes.df_datetime );
		this.to = (Date)PropertyTypes.castFromString( to, Date.class, PropertyTypes.df_datetime );
		this.created = (Date)PropertyTypes.castFromString( created, Date.class, PropertyTypes.df_date );
	}

	@Override
	public String getId() {
		return getReservationId();
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId( String reservationId ) {
		this.reservationId = reservationId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer( Customer customer ) {
		this.customer = customer;
	}

	public String getText() {
		return text;
	}

	public void setText( String text ) {
		this.text = text;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom( Date from ) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo( Date to ) {
		this.to = to;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated( Date created ) {
		this.created = created;
	}

}
