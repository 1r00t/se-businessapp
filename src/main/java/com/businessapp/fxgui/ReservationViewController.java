package com.businessapp.fxgui;

import java.util.ArrayList;
import java.util.Collection;

import com.businessapp.entity.EntityIntf;
import com.businessapp.entity.EntityProperties;
import com.businessapp.model.Reservation;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;


/**
 * UI Controller implementation that is associated with same name .fxml file.
 * <p>
 * ReservationViewController implements the controls of the reservations view.
 *
 */
public class ReservationViewController extends GenericEntityTableViewController<Reservation> {

	@FXML
	private AnchorPane FXID_ReservationsView;

	/**
	 * Method that allows to implement specific UI-logic of a ViewController.
	 */
	@Override
	public void buildView() {

		super.buildView( EntityProperties.get( Reservation.class ), "Reservation", new ContextMenuItemsDescriptor[] {
			//
			new ContextMenuItemsDescriptor<Reservation>( "Delete" ) {
				@Override
				public void onAction( Reservation e ) {
					deleteAction();
				}
			},
			new ContextMenuItemsDescriptor<Reservation>( "Update" ) {
				@Override
				public void onAction( Reservation e ) {
					updateAction();
				}
			}
		});
	}

	/**
	 * Method that allows to refresh a UI-view, e.g. to synchronize the state of the
	 * view with the state in the underlying functional (business) logic.
	 * <p>
	 * @return used to pass number (size) of refreshed entities to adjust selection in view accordingly.
	 */
	@Override
	public int refreshView() {
		table.getItems().clear();
		Collection<Reservation> sortedList = sort(
				businessLogicImpl.findAllReservations(), ( Reservation r1, Reservation r2 ) -> {
				String i1 = r1.getCustomer()==null? "(null)" : r1.getCustomer().getId();
				String i2 = r2.getCustomer()==null? "(null)" : r2.getCustomer().getId();
				return i1.compareTo( i2 );
			}
		);
		int size = sortedList.size();
		table.getItems().addAll( sortedList );
		return size;
	}

	/**
	 * Local method to handle update-actions initiated by update context menu or update button.
	 */
	@FXML
	void updateAction() {
		int i = table.getSelectionModel().getSelectedIndex();
		if( i >= 0 ) {
			EntityIntf entity = table.getItems().get( i );
			entryDialog( entity, eps, false, this );
		} else {
			System.out.println( "No selection." );
		}
	}

	/**
	 * Local method to handle delete-actions initiated by delete context menu or delete button.
	 */
	@FXML
	void deleteAction() {
		int i = table.getSelectionModel().getSelectedIndex();
		if( i >= 0 ) {
			String id = table.getItems().get( i ).getId();
			businessLogicImpl.deleteReservation( id );
			int size = refreshView();
			table.getSelectionModel().select( Math.min( i, size - 1 ) );
		} else {
			System.out.println( "No selection." );
		}
	}


	/*
	 * Private method(s).
	 */
	@FunctionalInterface
	private interface ComparatorIntf<T> {
		int compare( T e1, T e2 );
	}
	private Collection<Reservation>sort( Collection<Reservation> col, ComparatorIntf<Reservation> cif ) {
		ArrayList<Reservation>sl = new ArrayList<Reservation>();
		for( Reservation e : col ) {
			int i = 0;
			for( ; i < sl.size() && ( cif.compare( e, sl.get( i ) ) > 0 ); i++ );
			sl.add( i, e );
		}
		return sl;
	}

}
