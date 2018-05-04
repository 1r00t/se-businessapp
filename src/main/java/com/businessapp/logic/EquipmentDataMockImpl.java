package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Equipment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;


/**
 * Implementation of Customer data.
 *
 */
class EquipmentDataMockImpl implements EquipmentDataIntf {

	private final HashMap<String, Equipment> _data;	// HashMap as data container
	private final EquipmentDataIntf DS;				// Data Source/Data Store Intf
	private Component parent;						// parent component

	/**
	 * Constructor.
	 */
	EquipmentDataMockImpl() {
		this._data = new HashMap<String, Equipment>();
		this.DS = this;
	}

	/**
	 * Dependency injection methods.
	 */
	@Override
	public void inject( ControllerIntf dep ) {
	}

	@Override
	public void inject( Component parent ) {
		this.parent = parent;
	}

	/**
	 * Start.
	 */
	@Override
	public void start() {
		SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );

		String name = parent.getName();
		if( name.equals( "Katalog" ) ) {
			try {
				DS.newEquipment("Weste").setMaintenanceDate(df.parse("20.11.2018"));
				DS.newEquipment("Atemregler").setMaintenanceDate(df.parse("14.03.2019"));
				DS.newEquipment("Erste Stufe").setMaintenanceDate(df.parse("27.04.2018"));
				DS.newEquipment("Zweite Stufe").setMaintenanceDate(df.parse("12.07.2018"));
				DS.newEquipment("Flasche").setMaintenanceDate(df.parse("22.10.2020"));
				DS.newEquipment("Handschuhe");
				DS.newEquipment("Schuhe");
				DS.newEquipment("Maske");
				DS.newEquipment("Schnorchel");
				DS.newEquipment("Flossen");
				DS.newEquipment("Kompass");
				DS.newEquipment("Taschenlampe");
				DS.newEquipment("Anzug");
				DS.newEquipment("Haube");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public Equipment findEquipmentById( String id ) {
		return _data.get( id );
	}

	@Override
	public Collection<Equipment> findAllEquipments() {
		return _data.values();
	}

	@Override
	public Equipment newEquipment( String name ) {
		Equipment e = new Equipment(name);
		_data.put( e.getId(), e );
		//save( "created: ", c );
		return e;
	}

	@Override
	public void updateEquipment( Equipment e ) {
		String msg = "updated: ";
		if( e != null ) {
			Equipment e2 = _data.get( e.getId() );
			if( e != e2 ) {
				if( e2 != null ) {
					_data.remove( e2.getId() );
				}
				msg = "created: ";
				_data.put( e.getId(), e );
			}
			//save( msg, c );
			System.err.println( msg + e.getId() );
		}
	}

	@Override
	public void deleteEquipment( Collection<String> ids ) {
		String showids = "";
		for( String id : ids ) {
			_data.remove( id );
			showids += ( showids.length()==0? "" : ", " ) + id;
		}
		if( ids.size() > 0 ) {
			//save( "deleted: " + idx, customers );
			System.err.println( "deleted: " + showids );
		}
	}

}
