package com.businessapp.fxgui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.util.HashMap;

import com.businessapp.entity.EntityIntf;
import com.businessapp.entity.EntityProperties;
import com.businessapp.entity.EntityProperty;
import com.businessapp.logic.BusinessLogicIntf;
import com.businessapp.logic.LogicIntf;


/**
 * Abstract, parameterized (generic) UI Controller implementation for TableViews
 * on entity classes.
 * <p>
 * Purpose is to implement common functionality of a TableView on entity classes by
 * generically rendering table structures as well as create, update, delete controls
 * from EntityProperties that are associated with entity classes.
 * <p>
 * Known subclasses are:
 *	- BusinessCustomerViewController,
 *	- IndividualCustomerViewController,
 *	- ReservationViewController.
 * <p>
 * @param <T> type of entity class, e.g. Customer.
 */
abstract class GenericEntityTableViewController<T extends EntityIntf> implements ViewControllerIntf {
	private String label;

	protected TableView<T> table;
	protected EntityProperties eps;
	protected BusinessLogicIntf businessLogicImpl;

	/*
	 * Used to locate another ViewController instance from its class.
	 */
	protected static final HashMap< Class<? extends ViewControllerIntf>, ViewControllerIntf > findViewController =
			new HashMap< Class<? extends ViewControllerIntf>, ViewControllerIntf >();


	@FXML
	private AnchorPane FXID_AnchorPane;

	@FXML
	private TableView<T> FXID_EntityTable;

	@FXML
	private void initialize() {
		//System.out.println( "@FXML initialized called for: " + this.getClass().getName() );
	}

	/**
	 * Method to inject an anonymous implementation instance of the controller's
	 * functional (business) logic. Method is invoked during ViewController startup.
	 * <p>
	 * @param logicImpl anonymous implementation of the controller's functional (business) logic.
	 */
	@Override
	public void injectImpl( LogicIntf logicImpl ) {
		findViewController.put( this.getClass(), this );
		this.businessLogicImpl = (BusinessLogicIntf)logicImpl;
		buildView();
	}

	/*
	 * Used to create type-specific context menus.
	 */
	abstract class ContextMenuItemsDescriptor<T2> {
		final String name;
		ContextMenuItemsDescriptor( String name ) {
			this.name = name;
		}
		public abstract void onAction( T2 e );
	}

	/**
	 * Method to implement specific UI-logic of a GenericEntityTableViewController.
	 * <p>
	 * @param eps reference to EntityProperties associated with generic type.
	 * @param title title shown in dialog windows generated from this ViewController.
	 * @param contextMenuItemDescriptor description of context menu information (popup on right-click).
	 */
	void buildView( EntityProperties eps, String title, ContextMenuItemsDescriptor[] contextMenuItemDescriptor ) {
		this.label = title;
		this.table = FXID_EntityTable;
		this.eps = eps;

		for( int i=0; i < eps.getFieldSize(); i++ ) {
			TableColumn<T, String> tableCol = new TableColumn<>( eps.getEntityProperty( i ).getLabel() );
			//tableCol.getStyleClass().add( ep.getEntityProperty( i ).getColumnFormattingClass() );
			int colIdx = i;
			tableCol.setCellValueFactory(
				cell -> {
					T entity = cell.getValue();
					int colIndex = colIdx;
					SimpleStringProperty p = new SimpleStringProperty();
					p.set( eps.getEntityProperty( colIndex ).getFieldValueAsString( entity ) );
					//cell.getgetStyleClass().add( "col-left" );
					return p;
				}
			);
			if( i < table.getColumns().size() ) {
				table.getColumns().set( i, tableCol );
			} else {
				table.getColumns().add( i, tableCol );
			}
		}

		table.setRowFactory(
			new Callback<TableView<T>, TableRow<T>>() {
				@Override
				public TableRow<T> call( TableView<T> tableView ) {
					final TableRow<T> row = new TableRow<T>();
					final ContextMenu contextMenu = new ContextMenu();
					if( contextMenuItemDescriptor != null ) {
						for( ContextMenuItemsDescriptor<T> itemDescr : contextMenuItemDescriptor ) {
							final MenuItem menuItem = new MenuItem( itemDescr.name );
							menuItem.setOnAction( (ActionEvent e) -> {
								itemDescr.onAction( row.getItem() );
							});
							contextMenu.getItems().add( menuItem );
						}
					}
					// Set context menu on row, but use a binding to make it only show for non-empty rows:
					row.contextMenuProperty().bind(
						Bindings.when( row.emptyProperty() )
						.then( (ContextMenu)null )
						.otherwise( contextMenu )
					);
					return row;
				}
			});

		refreshView();
		table.getSelectionModel().select( 0 );
		//table.getColumns().setAll( firstNameCol, lastNameCol, actionCol );
		//table.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
	}

	/**
	 * Generate generic entry dialog window with input fields rendered from EntityProperties.
	 * <p>
	 * @param entity selected entity instance for which entry dialog window is created.
	 * 					Values of that instance are shown in generated input fields.
	 * @param eps EntityProperties associated with entity class.
	 * @param newEntityFlag indicator that dialog is for creation of a new entity.
	 * 					(null) is shown as values in generated input fields. 
	 * @param toRefresh ViewControllerIntf to be refreshed when <OK>-button is pressed.
	 * 					It can be a differrent ViewController than the one that triggered
	 * 					the method. For example: a new reservation is created on a customer
	 * 					view (with CustomerViewController calling entryDialog), but the
	 * 					new reservation needs to be refreshed in the ReservationViewController.
	 */
	void entryDialog( EntityIntf entity, EntityProperties eps, boolean newEntityFlag, ViewControllerIntf toRefresh ) {
		String[] fieldValues = eps.getFieldValues( entity );
		EntityEntryDialog dialog = new EntityEntryDialog( newEntityFlag, label, eps, fieldValues );

		// called when "OK" button in EntityEntryDialog is pressed
		dialog.addEventHandler( ActionEvent.ACTION, event -> {
			if( event.getTarget() == dialog ) {
				String idBefore = entity.getId();
				eps.setFieldValues( entity, fieldValues );
				String idAfter = entity.getId();
				if( ! idBefore.equals( idAfter ) ) {
					System.out.println( " -- " + GenericEntityTableViewController.class.getSimpleName() +
							": id's should not change during update." );
				}
				businessLogicImpl.save( entity );
				toRefresh.refreshView();
				table.getSelectionModel().select( (T)entity );
			}
		});
		dialog.show();
	}


	/*
	 * ************************************************************************
	 * Private class(es).
	 */
	private class EntityEntryDialog extends Stage {
		private final GridPane grid;
		private final TextField[] textFields;
		private final Button ok;
		private final Button cancel;

		private EntityEntryDialog( boolean newEntity, String title, EntityProperties eps, String[] fieldValues ) {
			this.setTitle( ( newEntity? "New " : "" ) + title );
			this.initModality( Modality.WINDOW_MODAL );
			this.initStyle( StageStyle.UTILITY );
			this.setWidth( 360 );	// hard-wired width, for now

			// create a grid for the data entry.
			textFields = new TextField[ fieldValues.length ];
			grid = new GridPane();
			for( int i=0; i < fieldValues.length; i++ ) {
				EntityProperty ep = eps.getEntityProperty( i );
				String fieldName = ep.getLabel();
				textFields[ i ] = new TextField();
				grid.addRow( i, new Label( fieldName ), textFields[ i ] );
				grid.setHgap( 10 );
				grid.setVgap( 10 );
				GridPane.setHgrow( textFields[ i ], Priority.ALWAYS );
				String fieldValue = fieldValues[ i ]==null? "" : fieldValues[ i ];
				textFields[ i ].setText( fieldValue );
				if( ! ep.isEditable() ) {
					textFields[ i ].setEditable( false );
					textFields[ i ].setStyle(
						"-fx-border-radius: 4px;" +
						"-fx-border-color: lightgrey;" +
						"-fx-background-color: #fafafa;"
					);
				}
			}

			// create action buttons for the dialog.
			ok = new Button( "OK" );
			ok.setDefaultButton( true );

			cancel = new Button( "Cancel" );
			cancel.setCancelButton( true );

			// add action handlers for the dialog buttons.
			ok.setOnAction( e -> {
				for( int i=0; i < fieldValues.length; i++ ) {
					fieldValues[ i ] = textFields[ i ].getText();
				}
				// propagate ActionEvent back to Dialog object
				this.fireEvent( new ActionEvent() );
				this.close();
			});

			cancel.setOnAction( e -> {
				this.close();
			});

			HBox buttons = new HBox();
			buttons.setSpacing( 10 );
			buttons.setAlignment( Pos.CENTER_RIGHT );
			buttons.getChildren().addAll( ok, cancel );

			VBox layout = new VBox( 20 );
			VBox vspacer = new VBox( 20 );	// add more vertical space to fully show buttons
			layout.getChildren().addAll( grid, buttons, vspacer );
			layout.setPadding( new Insets( 5 ) );
			this.setScene( new Scene( layout ) );
		}
	}

}
