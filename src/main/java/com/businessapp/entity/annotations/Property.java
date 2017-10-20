package com.businessapp.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Field annotation in entity classes to indicate that a field is an entity property
 * that is accessible through EntityProperties.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD } )
public @interface Property {

	public enum DateFormats { date, time, datetime };

	String label();

	DateFormats dateFormat() default DateFormats.date;

	boolean editable() default true;

}
