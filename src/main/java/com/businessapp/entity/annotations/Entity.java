package com.businessapp.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Class annotation to indicate that a class is an Entity class.
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.TYPE } )
public @interface Entity {

}
