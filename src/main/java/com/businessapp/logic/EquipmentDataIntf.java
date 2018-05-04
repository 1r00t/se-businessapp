package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Equipment;

import java.util.Collection;

/**
 * Public interface to Customer data. 
 *
 */
public interface EquipmentDataIntf extends ControllerIntf {

	/**
	 * Factory method that returns a Customer data source.
	 * @return new instance of Customer data source.
	 */
	public static EquipmentDataIntf getController() {
		return new EquipmentDataMockImpl();
	}

	/**
	 * Public access methods to Customer data.
	 */
	Equipment findEquipmentById(String id);

	public Collection<Equipment> findAllEquipments();

	public Equipment newEquipment(String name);

	public void updateEquipment(Equipment e);

	public void deleteEquipment(Collection<String> ids);

}
