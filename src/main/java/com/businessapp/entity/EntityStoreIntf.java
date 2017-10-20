package com.businessapp.entity;

import java.util.Collection;


/**
 * Public interface of EntityStore. An EntityStore instance stores entity
 * objects of the same class.
 */
public interface EntityStoreIntf {

	public enum ID_CHARSET { ALPHA_NUM, ALPHA_NUM_UPPER, DEC, HEX, BIN };
	/**
	 * Public static method to create idFormat String from arguments.
	 * <p>
	 * Each entity has a unique identifier. Identifiers of an entity class are Strings
	 * that may differ in terms of the character set, prefix or number of digits used.
	 * IdFormat allows to describe the id representation.
	 * Examples:
	 *	"ALPHA_NUM_UPPER#6" - a 6-digit alpha-num identifier, e.g. "X8BA40"
	 *	"C_DEC#8" - a 8-digit decimal identifer prefixed by "C", e.g. "C36874215"
	 * <p>
	 * @param charSet ID_CHARSET used for identifiers (ALPHA_NUM, ALPHA_NUM_UPPER, DEC, HEX, BIN).
	 * @param prefix null or prefix used for identifier.
	 * @param digits number of digits used.
	 * @return idFormat String.
	 */
	static String getIdFormat( ID_CHARSET charSet, String prefix, int digits ) {
		return IDGen.getIdFormat( charSet, prefix, digits );
	}

	String nextId();					// return next id that is not yet in use in the store

	void save( EntityIntf e );			// save entity to store

	void delete( String id );			// delete entity from store

	EntityIntf findById( String id );	// find entity in store by id

	Collection<EntityIntf>findAll();	// return all store entities

	boolean containsKey( String id );	// true if id is present in store

	int size();							// number of entities currently in store

}
