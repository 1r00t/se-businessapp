package com.businessapp.fxgui;

import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import com.businessapp.entity.EntityIntf;
import com.businessapp.entity.EntityProperties;
import com.businessapp.model.BusinessCustomer;
import com.businessapp.model.Customer;
import com.businessapp.model.Reservation;


/**
 * UI Controller implementation that is associated with same name .fxml file.
 * <p>
 * BusinessCustomerViewController implements the controls of the business customers view.
 *
 */
public class BusinessCustomerViewController extends GenericEntityTableViewController<Customer> {

	@FXML
	private AnchorPane FXID_BusinessCustomersView;

	/**
	 * Method that allows to implement specific UI-logic of a ViewController.
	 */
	@Override
	public void buildView() {

		super.buildView( EntityProperties.get( BusinessCustomer.class ), "Customer", new ContextMenuItemsDescriptor[] {
			//
			new ContextMenuItemsDescriptor<Customer>( "Delete" ) {
				@Override
				public void onAction( Customer e ) {
					deleteAction();
				}
			},
			new ContextMenuItemsDescriptor<Customer>( "Update" ) {
				@Override
				public void onAction( Customer e ) {
					updateAction();
				}
			},
			new ContextMenuItemsDescriptor<Customer>( "New Customer" ) {
				@Override
				public void onAction( Customer e ) {
					createAction();
				}
			},
			new ContextMenuItemsDescriptor<Customer>( "New Reservation" ) {
				@Override
				public void onAction( Customer e ) {
					addReservationAction( e );
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
		ObservableList<Customer> oList = FXCollections.observableArrayList(
				businessLogicImpl.findAllCustomers()
		);
		FilteredList<Customer> filteredList = new FilteredList<Customer>( oList, s -> {
			return s instanceof BusinessCustomer;
		} );
		SortedList<Customer> sortedList = new SortedList<Customer>( filteredList );
		sortedList.setComparator( Comparator.comparing( Customer::getName ) );
		int size = sortedList.size();
		table.getItems().addAll( sortedList );
		return size;
	}

	/**
	 * Local method to handle create-actions initiated by create context menu or create button.
	 */
	@FXML
	void createAction() {
		entryDialog( businessLogicImpl.newBusinessCustomer(), eps, true, this );
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
			businessLogicImpl.deleteCustomer( id );
			int size = refreshView();
			table.getSelectionModel().select( Math.min( i, size - 1 ) );
		} else {
			System.out.println( "No selection." );
		}
	}

	/**
	 * Local method to handle addReservation-actions initiated by addReservation context menu or addReservation button.
	 */
	@FXML
	void addReservationAction() {
		int i = table.getSelectionModel().getSelectedIndex();
		if( i >= 0 ) {
			Customer c = table.getItems().get( i );
			addReservationAction( c );
		} else {
			System.out.println( " -- no selection.");
		}
	}


	/*
	 * Private method(s).
	 */
	private void addReservationAction( Customer c ) {
		Reservation newR = businessLogicImpl.newReservation( c );
		entryDialog( newR, EntityProperties.get( Reservation.class ), true,
			findViewController.get( ReservationViewController.class )
		);
	}

}
