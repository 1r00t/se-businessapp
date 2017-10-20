package com.businessapp.logic;

import java.util.Collection;
import java.util.Date;

import com.businessapp.entity.EntityStoreIntf;
import com.businessapp.entity.EntityFactory;
import com.businessapp.entity.EntityIntf;
import com.businessapp.entity.PropertyTypes;
import com.businessapp.model.BusinessCustomer;
import com.businessapp.model.Customer;
import com.businessapp.model.IndividualCustomer;
import com.businessapp.model.Reservation;


/**
 * Hidden implementation of business logic.
 */
class BusinessLogic implements BusinessLogicIntf {
	private final EntityStoreIntf customerStore;
	private final EntityStoreIntf reservationStore;

	/**
	 * Local constructor. Invoked by factory.
	 */
	BusinessLogic() {
		this.customerStore = EntityFactory.newEntityStore(
				Customer.class,
				EntityStoreIntf.getIdFormat( EntityStoreIntf.ID_CHARSET.DEC, "C", 10 )		
		);
		this.reservationStore = EntityFactory.newEntityStore(
				Reservation.class,
				EntityStoreIntf.getIdFormat( EntityStoreIntf.ID_CHARSET.ALPHA_NUM_UPPER, null, 6 )
		);
	}

	@Override
	public Customer newIndividualCustomer() {
		Customer c = new IndividualCustomer();
		customerStore.save( c );
		return c;
	}

	@Override
	public Customer newBusinessCustomer() {
		Customer c = new BusinessCustomer();
		customerStore.save( c );
		return c;
	}

	private Reservation newReservation( String customerId, String text, String from, String to ) {
		Customer c = (Customer)customerStore.findById( customerId );
		String created = PropertyTypes.castToString( new Date(), PropertyTypes.df_date );
		Reservation r = new Reservation(
			reservationStore.nextId(), c, text, from, to, created
		);
		reservationStore.save( r );
		return r;
	}

	@Override
	public Reservation newReservation( Customer c ) {
		String created = PropertyTypes.castToString( new Date(), PropertyTypes.df_date );
		Reservation r = new Reservation(
			reservationStore.nextId(), c, null, null, null, created
		);
		reservationStore.save( r );
		return r;
	}

	@Override
	public Collection<Customer>findAllCustomers() {
		return (Collection<Customer>)(Object)customerStore.findAll();
	}

	@Override
	public Collection<Reservation>findAllReservations() {
		return (Collection<Reservation>)(Object)reservationStore.findAll();
	}

	@Override
	public void deleteCustomer( String id ) {
		customerStore.delete( id );
	}

	@Override
	public void deleteReservation( String id ) {
		reservationStore.delete( id );
	}

	@Override
	public void save( EntityIntf e ) {
		if( e != null ) {
			EntityFactory.getEntityStore( e.getClass() ).save( e );
		}
	}

	@Override
	public int size( Class<? extends EntityIntf>eType ) {
		return EntityFactory.getEntityStore( eType ).size();
	}

	@Override
	public String nextId( Class<? extends EntityIntf>eType ) {
		return EntityFactory.getEntityStore( eType ).nextId();
	}

	@Override
	public EntityIntf findById( Class<? extends EntityIntf>eType, String id ) {
		return EntityFactory.getEntityStore( eType ).findById( id );
	}

	@Override
	public void loadData() {
		IndividualCustomer p1 = new IndividualCustomer( "C4826760136", "15.09.2016", "Larry",	"Hagman"	);
		IndividualCustomer p2 = new IndividualCustomer( "C9263983246", "20.09.2017", "Tilo",	"Naumann"	);
		BusinessCustomer c1 = new BusinessCustomer( "C9645834246", "20.03.2004", "Logos GmbH",	"q34-985-19346"	);
		BusinessCustomer c2 = new BusinessCustomer( "C9646634246", "14.08.2012", "Siemens",		"q34-823-67925"	);
		customerStore.save( p1 );
		customerStore.save( p2 );
		customerStore.save( c1 );
		customerStore.save( c2 );
		newReservation( p1.getId(), "BMW 320d",	"15.09.2017 8:00", "17.09.2017 08:00" );
		newReservation( p1.getId(), "MB C-Class","15.16.2017 8:00", "15.19.2017 18:00" );
		newReservation( p1.getId(), "VW Polo",	"15.09.2017 8:00", "15.09.2017 20:00" );
		newReservation( p1.getId(), "MB E-Class","25.09.2017 8:00", "28.09.2017 20:00" );

		customerStore.save( new IndividualCustomer( "C8645682091", "08.09.2012", "Alois",	"Werner"	) );
		customerStore.save( new IndividualCustomer( "C0957012561", "22.08.2007", "Edina",	"Klamm"		) );
		customerStore.save( new IndividualCustomer( "C8403254514", "19.04.2011", "Eric",	"Meyer"		) );
		customerStore.save( new IndividualCustomer( "C6692178625", "03.12.2014", "Daniel",	"Frahn"		) );
		customerStore.save( new IndividualCustomer( "C8758712569", "02.05.2014", "Max",		"Meyer"		) );
		customerStore.save( new IndividualCustomer( "C8721335422", "15.02.2006", "Susann",	"Fink"		) );
		customerStore.save( new IndividualCustomer( "C8932587136", "18.07.2002", "Maria",	"Sommer"	) );
	}

}
