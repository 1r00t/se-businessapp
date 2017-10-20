package com.businessapp.entity;

import java.util.Collection;
import java.util.HashMap;


/**
 * Local implementation class of EntityStoreIntf.
 *
 */
class EntityStore implements EntityStoreIntf {
	private final IDGen idGen;
	private final HashMap< String, EntityIntf > entityList;


	/*
	 * Local constructor.
	 */
	EntityStore( IDGen idGen ) {
		this.idGen = idGen;
		this.entityList = new HashMap< String, EntityIntf > ();
	}

	/*
	 * Interface method(s).
	 */
	@Override
	public String nextId() {
		String id;
		do {
			id = idGen.generateNextId();
		} while( entityList.containsKey( id ) );
		return id;
	}

	@Override
	public void save( EntityIntf e ) {
		String cid = e.getId();
		EntityIntf e2 = entityList.get( cid );
		if( e2 == null ) {
			// add new object to List
			entityList.put( cid, e );
		} else {
			// existing object in List
			if( e2 != e ) {
				entityList.remove( cid );
				entityList.put( cid, e );
			}
		}
	}

	@Override
	public void delete( String id ) {
		entityList.remove( id );
	}

	@Override
	public EntityIntf findById( String id ) {
		return entityList.get( id );
	}

	@Override
	public Collection<EntityIntf>findAll() {
		return entityList.values();
	}

	@Override
	public boolean containsKey( String id ) {
		return entityList.containsKey( id );
	}

	@Override
	public int size() {
		return entityList.size();
	}

}
