package com.businessapp.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.businessapp.entity.annotations.Entity;
import com.businessapp.entity.annotations.Property;
import com.businessapp.entity.annotations.ID;


/**
 * EntityProperties represents the set of EntityProperty instances each representing a
 * Property-annotated fields of entity classes.
 * <p>
 * Instances of class EntityProperties exist one for each entity class. The are obtained
 * by introspection of entity class looking for entity annotations Entity, Property, ID.
 * They are used to provide unreferenced access to fields, e.g. in oder to generate input
 * dialogs for entity property values.
 */
public class EntityProperties {
	private static final HashMap< Class<? extends EntityIntf>, EntityProperties > epsMap =
			new HashMap< Class<? extends EntityIntf>, EntityProperties >();

	private Class<? extends EntityIntf> clazz;
	private EntityProperty id = null;
	private final ArrayList<EntityProperty>props;

	/**
	 * Public static method to return EntityProperties instance that is associated
	 * with an entity class.
	 * <p>
	 * @param clazz entity class for which an EntityProperties instance is returned.
	 * @return EntityProperties instance that is associated with entity class.
	 */
	public static EntityProperties get( Class<? extends EntityIntf>clazz ) {
		EntityProperties found = epsMap.get( clazz );
		if( found==null ) {
			found = new EntityProperties( clazz );
			epsMap.put( clazz, found );
		}
		return found;
	}

	/**
	 * Private constructor.
	 * <p>
	 * @param clazz entity class for which an EntityProperties instance is created.
	 */
	private EntityProperties( Class<? extends EntityIntf> clazz ) {
		this.clazz = clazz;
		//System.out.println(" -- " + EntityProperties.class.getName()
		//		+ " entry created for: " + clazz.getName() );
		this.props = collectPropertyFields( clazz, new ArrayList<EntityProperty>() );
		if( id==null ) {
			System.err.println( "No ID (primary key) found in class: " + clazz.getName() );
		}
	}

	/**
	 * Return entity class that is associated with EntityProperties instance.
	 * <p>
	 * @return entity class that is associated with EntityProperties instance.
	 */
	public Class<? extends EntityIntf> getClazz() {
		return clazz;
	}

	/**
	 * Return size of EntityProperties instance, which is the number of EntityProperty fields.
	 * <p>
	 * @return size of EntityProperties instance, which is the number of EntityProperty fields.
	 */
	public int getFieldSize() {
		return props.size();
	}

	/**
	 * Return the i-th EntityProperty in EntityProperties.
	 * <p>
	 * @param i index of EntityProperty in EntityProperties.
	 * @return i-th EntityProperty in EntityProperties.
	 */
	public EntityProperty getEntityProperty( int i ) {
		return props.get( i );
	}

	/**
	 * Return field values of entity instance as String[].
	 * <p>
	 * @param instance entity instance for which field values are returned.
	 * @return field values of entity instance.
	 */
	public String[] getFieldValues( EntityIntf instance ) {
		String[] values = new String[ getFieldSize() ];
		for( int i=0; i < getFieldSize(); i++ ) {
			values[ i ] = props.get( i ).getFieldValueAsString( instance );
		}
		return values;
	}

	/**
	 * Set values of entity instance in order as passed in String[].
	 * <p>
	 * @param instance instance entity instance of which field values will be set.
	 * @param values array of String values that are set as field values of entity instance.
	 */
	public void setFieldValues(  EntityIntf instance, String[] values ) {
		for( int i=0; values != null && i < Math.min( getFieldSize(), values.length ); i++ ) {
			String value = values[ i ];
			props.get( i ).setFieldValueAsString( instance, value );
		}
	}

	/**
	 * Helper method to return JSON String rendering of entity instance values.
	 * <p>
	 * @param instance entity instance for which JSON String rendering is returned.
	 * @return JSON String rendering of entity instance value
	 */
	public String printable( EntityIntf instance ) {
		StringBuffer sb = null;
		String[] values = getFieldValues( instance );
		sb = new StringBuffer();
		sb.append( "{ " );
		for( int i=0; i < getFieldSize(); i++ ) {
			if( i > 0 ) {
				sb.append( ", " );
			}	// print JSON-style
			sb.append( "\"" ).append( getEntityProperty( i ).getPropertyName() ).append( "\"" );
			sb.append( ": " );
			sb.append( "\"" ).append( values[ i ] ).append( "\"" );
		}
		sb.append( " }" );
		return sb.toString();
	}


	/*
	 * Private method(s).
	 */
	private boolean isEntityClass( Class<?> cls ) {
		return cls != null && cls.getAnnotation( Entity.class ) != null;
	}

	private ArrayList<EntityProperty> collectPropertyFields( Class<?> cls, ArrayList<EntityProperty>fL ) {
		if( isEntityClass( cls ) ) {
			Class<?>superCls = cls.getSuperclass();
			if( isEntityClass( superCls ) ) {
				// traverse inheritance hierarchy up to top-level Entity class
				collectPropertyFields( superCls, fL );
			}
			for( Field f : cls.getDeclaredFields() ) {
				Property propAnno = f.getAnnotation( Property.class );
				ID idAnno = f.getAnnotation( ID.class );
				if( propAnno != null || idAnno != null ) {
					EntityProperty ep = new EntityProperty( f );
					fL.add( ep );
					// sanity test for presence of one @ID field
					if( idAnno != null ) {
						if( id != null ) {
							System.err.println( "Multiple ID's (primary keys) found: "
									+ f.getName() + " ignored in class: " + cls.getName() );
						} else {
							id = ep;
						}
					}
					// sanity test that property type is supported
					if( ! ( PropertyTypes.isSupportedBaseType( f.getType() ) || isEntityClass( f.getType() ) ) ) {
						System.err.println( "Property " + f.getName() + " - unsupported property type: "
								+ f.getType() + " in class: " + cls.getName() );
					}
					ep.testGetterSetterMethodsPresence( cls, f );
				}
			}
		}
		return fL;
	}

}
