package com.businessapp.pojos;

import java.io.Serializable;


/**
 * Public entity interface.
 *
 */
public interface EntityIntf extends Serializable {

	/**
	 * Return entity identifier.
	 * @return entity identifier.
	 */
	public abstract String getId();

}
