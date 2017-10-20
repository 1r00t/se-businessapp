package com.businessapp.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.businessapp.entity.annotations.Property;
import com.businessapp.logic.BusinessLogicIntf;
import com.businessapp.logic.LogicFactory;


/**
 * PropertyTypes allows conversion of values to/from Strings of types that are
 * supported by Property-annotated fields in entity classes.
 */
public class PropertyTypes {

	public static final SimpleDateFormat df_date = new SimpleDateFormat( "dd.MM.yyyy" );
	public static final SimpleDateFormat df_time = new SimpleDateFormat( "HH:mm:ss" );
	public static final SimpleDateFormat df_datetime = new SimpleDateFormat( "dd.MM.yyyy HH:mm" );

	public static final String NULL_AS_STR = "(null)";

	/*
	 * Java primitive types: byte, short, int, long, float, double, boolean, char
	 */
	private static final ArrayList<Class<?>> javaBaseTypes = new ArrayList<Class<?>>( Arrays.asList( new Class<?>[] {
		String.class,
		long.class, Long.class,
		int.class, Integer.class,
		boolean.class, Boolean.class,
		java.util.Date.class,
		double.class, Double.class,
		float.class, Float.class,
		char.class,
		byte.class, Byte.class,
		short.class, Short.class,
	}));


	/**
	 * Local static method to test whether a type or class is supported as @Property-annotated field in entity classes.
	 * <p>
	 * @param clazz class or type to test.
	 * @return boolean result whether a type or class is supported as @Property-annotated field.
	 */
	static boolean isSupportedBaseType( Class<?> clazz ) {
		return	javaBaseTypes.contains( clazz ) ||
				EntityIntf.class.isAssignableFrom( clazz );
	}

	/**
	 * Local static method to cast value from String to the native type defined by toClass.
	 * <p>
	 * @param val value as String.
	 * @param toClazz type to convert value to.
	 * @param prop access to further information included in annotation, e.g. date format.
	 * @return value as native type defined by toClazz.
	 */
	static Object castFromString( String val, Class<?> toClazz, Property prop ) {
		return castFromString( val, toClazz, getDateFormatFromProperty( prop ) );
	}

	/**
	 * Local static method to cast value from String to the native type defined by toClass.
	 * <p>
	 * @param val value as String.
	 * @param toClazz type to convert value to.
	 * @param df date format used for date conversion.
	 * @return value as native type defined by toClazz.
	 */
	public static Object castFromString( String val, Class<?> toClazz, SimpleDateFormat df ) {
		if( val==null || val.equals( NULL_AS_STR ) ) {
			return null;
		}
		try {
			switch( toClazz.getName() ) {
			case "String.class":	return (String)val;
									//Long.parseLong( val );
			case "long":			return (long)Long.valueOf( val ).longValue();
			case "int":				return (int)Integer.valueOf( val ).intValue();
			case "boolean":			return (boolean)Boolean.valueOf( val ).booleanValue();
			case "double":			return (double)Double.valueOf( val ).doubleValue();
			case "float":			return (float)Float.valueOf( val ).floatValue();
			case "char":			return (char)val.charAt( 0 );
			
			case "java.util.Date":
				return (Date)df.parse( val );
									// new Long( Long.parseLong( val ) );
			case "Long.class":		return Long.valueOf( val );
			case "Integer.class":	return Integer.valueOf( val );
			case "Boolean.class":	return Boolean.valueOf( val );
			case "Double.class":	return Double.valueOf( val );
			case "Float.class":		return Float.valueOf( val );
	
			case "byte":			return (byte)Byte.valueOf( val ).byteValue();
			case "Byte.class":		return Byte.valueOf( val );
			case "short":			return (short)Short.valueOf( val ).shortValue();
			case "Short.class":		return Short.valueOf( val );
			}

		} catch( NumberFormatException e ) {
			System.err.println( "--> Number/FormatException " + e.getMessage() );
		} catch( ParseException e ) {
			System.err.println( "--> Parse/Exception " + e.getMessage() );
		}

		if( EntityIntf.class.isAssignableFrom( toClazz ) ) {
			BusinessLogicIntf bl = LogicFactory.getBusinessLogic();
			Object e = bl.findById( (Class<? extends EntityIntf>) toClazz, val );
			return e;
		}

		return val;
	}

	/**
	 * Local static method to convert/cast object value from native type to String.
	 * <p>
	 * @param val native value.
	 * @param prop access to further information included in annotation, e.g. date format.
	 * @return value converted to String.
	 */
	static String castToString( Object val, Property prop ) {
		return castToString( val, getDateFormatFromProperty( prop ) );
	}

	/**
	 * Local static method to convert/cast object value from native type to String.
	 * <p>
	 * @param val native value.
	 * @param df date format used for date conversion.
	 * @return value converted to String.
	 */
	public static String castToString( Object val, SimpleDateFormat df ) {
		if( val==null ) {
			return NULL_AS_STR;
		}
		switch( val.getClass().getName() ) {
		case "String.class":	return (String)val;
		case "long":			return String.valueOf( (long)val );
		case "int":				return String.valueOf( (int)val );
		case "boolean":			return String.valueOf( (boolean)val );
		case "double":			return String.valueOf( (double)val );
		case "float":			return String.valueOf( (float)val );
		case "char":			return String.valueOf( (char)val );
		
		case "java.util.Date":
			return df.format( (Date)val );

		case "Long.class":		return String.valueOf( ((Long)val).longValue() );
		case "Integer.class":	return String.valueOf( ((Integer)val).intValue() );
		case "Boolean.class":	return String.valueOf( ((Boolean)val).booleanValue() );
		case "Double.class":	return String.valueOf( ((Double)val).doubleValue() );
		case "Float.class":		return String.valueOf( ((Float)val).floatValue() );

		case "byte":			return String.valueOf( (byte)val );
		case "Byte.class":		return String.valueOf( ((Byte)val).byteValue() );
		case "short":			return String.valueOf( (short)val );
		case "Short.class":		return String.valueOf( ((Short)val).shortValue() );
		}

		if( val instanceof EntityIntf ) {
			EntityIntf ei = (EntityIntf)val;
			return ei.getId();
		}

		return val.toString();
	}


	/*
	 * Private method(s).
	 */
	private static SimpleDateFormat getDateFormatFromProperty( Property prop ) {
		SimpleDateFormat df = PropertyTypes.df_date;
		if( prop != null ) {
			switch( prop.dateFormat() ) {
			case date:		df = PropertyTypes.df_date; break;
			case time:		df = PropertyTypes.df_time; break;
			case datetime:	df = PropertyTypes.df_datetime; break;
			}
		}
		return df;
	}

}
