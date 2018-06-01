package com.businessapp.pojos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

import com.businessapp.logic.IDGen;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.oracle.tools.packager.Log;


/**
 * Customer is an entity that represents a person (or a business)
 * to which a business activity can be associated.
 *
 */
public class Customer implements EntityIntf  {
	private static final long serialVersionUID = 1L;

	private static IDGen IDG = new IDGen( "C.", IDGen.IDTYPE.AIRLINE, 6 );

	// Customer states.
	public enum CustomerStatus { ACTIVE, SUSPENDED, TERMINATED };


	/*
	 * Properties.
	 */
	private String id = null;

	private String firstName = null;

	private String lastName = null;

	private List<String> contacts = new ArrayList<String>();

	private List<LogEntry> notes = new ArrayList<LogEntry>();

	private CustomerStatus status = CustomerStatus.ACTIVE;


	/**
	 * Private default constructor (required by JSON deserialization).
	 */
	@SuppressWarnings("unused")
	private Customer() { }

	/**
	 * Public constructor.
	 * @param id if customer id is null, an id is generated for the new customer object.
	 * @param firstName customers first name.
	 * @param lastName customers last name.
	 */
	public Customer( String id, String firstName, String lastName ) {
		this.id = id==null? IDG.nextId() : id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.notes.add( new LogEntry( "Customer record created." ) );
	}
/*	public Customer( String id, String name) {
		this.id = id==null? IDG.nextId() : id;
		this.firstName = name.substring(0, name.indexOf(" "));
		this.lastName = name.substring(name.indexOf(" "), name.length());;
		this.notes.add( new LogEntry( "Customer record created." ) );
	}*/


	/**
	 * Public getter/setter methods.
	 */
	public String getId() {
		return id;
	}

	/*public String getName() {
		return firstName + " " + lastName;
	}*/

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public List<String> getContacts() {
		return contacts;
	}

	@JsonGetter("notes")
	public List<String> getNotesAsStringList() {
		List<String>res = new ArrayList<>();
		for( LogEntry logEntry : getNotes() ) {
			res.add( logEntry.toString() );
		}
		return res;
	}

	@JsonSetter("notes")
	public Customer setNotesAsStringList(String[] notesAsStr) {
	    for (String noteAsStr: notesAsStr) {
            LogEntry note = new LogEntry(noteAsStr);
            getNotes().add(note);
        }
        return this;
    }

	public List<LogEntry> getNotes() {
		return notes;
	}

	public void setNote(String note) {
		this.notes.add(new LogEntry(note));
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public Customer setFirstName( String firstName ) {
		this.firstName = firstName;
		return this;
	}

	public Customer setLastName( String lastName ) {
		this.lastName = lastName;
		return this;
	}

	public Customer addContact( String contact ) {
		contacts.add( contact );
		return this;
	}

	public Customer setStatus( CustomerStatus status ) {
		this.status = status;
		return this;
	}


}
