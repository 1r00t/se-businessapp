package com.businessapp.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Field annotation in entity classes to indicate that a field is the identifier
 * property that is required for all entity classes.
 * Each entity class must have one @ID-annotated property.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD } )
public @interface ID {
	boolean editable() default false;	// ID annotated properties are not editable by default
}
