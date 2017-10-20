package com.businessapp.entity;

import java.util.HashMap;


/**
 * Public singleton factory to create and provide access to
 *	- EntityStore instances.
 *
 */
public class EntityFactory {
	private static EntityFactory singleton = new EntityFactory();

	/*
	 * Map that contains EntityStore objects hashed by entity classes.
	 */
	private final HashMap< Class<? extends EntityIntf>, EntityStore > entityStoreMap;

	/*
	 * Private constructor.
	 */
	private EntityFactory() {
		this.entityStoreMap = new HashMap< Class<? extends EntityIntf>, EntityStore >();
	}

	/**
	 * Public static access method to EntityStore of the specified entity class.
	 * <p>
	 * @param clazz entity class for which EntityStore reference is returned.
	 * @return EntityStore reference for the specified entity class.
	 */
	public static final EntityStoreIntf getEntityStore( Class<? extends EntityIntf> clazz ) {
		return newEntityStore( clazz, null );
	}

	/**
	 * Public static access method to EntityStore of the specified entity class.
	 * <p>
	 * @param clazz entity class for which EntityStore is created and reference returned.
	 * @param idFormat identifier format used in EntityStore.
	 * @return EntityStore reference for the specified entity class.
	 */
	public static final EntityStoreIntf newEntityStore( Class<? extends EntityIntf> clazz, String idFormat ) {
		EntityStore est = singleton.entityStoreMap.get( clazz );
		if( est==null ) {
			est = new EntityStore( new IDGen( idFormat ) );
			singleton.entityStoreMap.put( clazz,  est );
		}
		return est;
	}

}
