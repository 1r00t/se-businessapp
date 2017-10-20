package com.businessapp.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.businessapp.entity.annotations.ID;
import com.businessapp.entity.annotations.Property;


/**
 * EntityProperty represents one @Property-annotated field of an entity class.
 * <p>
 * Instances of class EntityProperty are aggregated in an associated EntityProperties class.
 */
public class EntityProperty {
	private final Field field;
	private int errs = 0;

	/**
	 * Local constructor.
	 * @param f field of an entity class.
	 */
	EntityProperty( Field f ) {
		this.field = f;
	}

	/**
	 * Return label of associated field.
	 * @return field label.
	 */
	public String getLabel() {
		return field.getAnnotation( Property.class ).label();
	}

	/**
	 * Return name of associated field.
	 * @return field name.
	 */
	String getPropertyName() {
		return field.getName();
	}

	/**
	 * Return @Property information whether property is editable in UI input dialog.
	 * @return information whether property is editable in UI input dialog.
	 */
	public boolean isEditable() {
		Property p = field.getAnnotation( Property.class );
		ID id = field.getAnnotation( ID.class );
		boolean editable = p != null? p.editable() : true;
		editable = id != null? id.editable() : editable;
		return editable;
	}

	/**
	 * Return field value of entity instance as or converted to String.
	 * @param instance entity instance.
	 * @return field value as or converted to String.
	 */
	public String getFieldValueAsString( Object instance ) {
		String methName = getterMethName( field, true );
		try {
			Method meth = instance.getClass().getMethod( methName );
			Object result = meth.invoke( instance );
			result = PropertyTypes.castToString( result, field.getAnnotation( Property.class ) );
			return (String)result;

		} catch( InvocationTargetException e ) {
			e.printStackTrace();
		} catch( IllegalAccessException e ) {
			e.printStackTrace();
		} catch( NoSuchMethodException e ) {
			if( errs++ % 100 == 0 ) {	// log only every 100th exception
				System.err.println( "    -- no method: " + instance.getClass().getName() + "." + methName + "(); - null returned." );
			}
		}
		return null;
	}

	/**
	 * Set field value of entity instance as or converted from a String.
	 * @param instance entity instance.
	 * @param value to set as field value.
	 */
	public void setFieldValueAsString( Object instance, String value ) {
		String methName = setterMethName( field, true );
		try {
			Method meth = null;
			Object arg = value;
			try {
				meth = instance.getClass().getMethod( methName, field.getType() );
				if( value != null && field.getType() != value.getClass() ) {
					arg = PropertyTypes.castFromString( value, field.getType(), field.getAnnotation( Property.class ) );
				}

			} catch( NoSuchMethodException e ) {
				meth = instance.getClass().getMethod( methName, String.class );
			}
			meth.invoke( instance, arg );

		} catch( InvocationTargetException e ) {
			e.printStackTrace();
		} catch( IllegalAccessException e ) {
			e.printStackTrace();
		} catch( NoSuchMethodException e ) {
			System.err.println( "    -- no method: " + instance.getClass().getName() + "." + methName + "( "
					+ field.getType().getName() + " arg ); - ignored." );
		}
	}

	/**
	 * Local method to check sanity of entity classes by testing for presence of setter/getter methods.
	 * @param clazz Entity class to be tested.
	 * @param f field for which sanity test will be carried out (do getter/setter methods exist for f?).
	 */
	void testGetterSetterMethodsPresence( Class<?> clazz, Field f ) {
		testMethodsPresence( clazz, f, f.getType(), false );
	}


	/*
	 * Private method(s).
	 */
	private void testMethodsPresence( Class<?> cls, Field f, Class<?> fieldType, boolean asString ) {
		String setterMethName = setterMethName( f, asString );
		try {
			cls.getMethod( setterMethName, fieldType );

		} catch( NoSuchMethodException e ) {
			System.err.println( "No setter: " + cls.getName() + "." + setterMethName + "( "
					+ fieldType.getName() + " arg );" );
		}
		String getterMethName = getterMethName( f, asString );
		try {
			Method meth = cls.getMethod( getterMethName );
			if( meth.getReturnType() != fieldType ) {
				throw new NoSuchMethodException( cls.getName() + "." + getterMethName + "() - "
						+ "return type (" + meth.getReturnType().getName() + ") "
						+ "does not match expected type (" + fieldType.getName() + ")" );
			}

		} catch( NoSuchMethodException e ) {
			System.err.println( "No getter: " + e.getMessage() );
		}

		if( ! PropertyTypes.isSupportedBaseType( fieldType ) ) {
			testMethodsPresence( cls, f, String.class, true );
		}
	}

	private final static String GETTER_PREFIX	= "get";
	private final static String SETTER_PREFIX	= "set";
	private final static String ASSTRING_SUFFIX	= "AsString";

	private String getterMethName( Field f, boolean asString ) {
		return methName( GETTER_PREFIX, f, asString );
	}

	private String setterMethName( Field f, boolean asString ) {
		return methName( SETTER_PREFIX, f, asString );
	}

	private String methName( String prefix, Field f, boolean asString ) {
		String fieldName = f.getName();
		StringBuffer methName = new StringBuffer( prefix )
			.append( fieldName.substring( 0, 1 ).toUpperCase() )
			.append( fieldName.substring( 1 ) );

		if( asString && ! PropertyTypes.isSupportedBaseType( f.getType() ) ) {
			methName.append( ASSTRING_SUFFIX );
		}
		return methName.toString();
	}

}
