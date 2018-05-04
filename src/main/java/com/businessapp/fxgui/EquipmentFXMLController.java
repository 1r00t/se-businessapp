package com.businessapp.fxgui;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.EquipmentDataIntf;
import com.businessapp.pojos.Equipment;
import com.businessapp.pojos.Equipment.EquipmentStatus;
import com.businessapp.pojos.LogEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FXML Controller class for Customer.fxml
 * 
 */
public class EquipmentFXMLController implements FXMLControllerIntf {
	private EquipmentDataIntf DS;

	/**
	 * FXML skeleton defined as:
	 * AnchorPane > GridPane > TableView	- GridPane as resizable container for TableView
	 * AnchorPane > HBox > Button			- buttons in footer area
	 * 
	 * Defined CSS style classes:
	 *   .tableview-customer-column-id
	 *   .tableview-customer-column-name
	 *   .tableview-customer-column-status
	 *   .tableview-customer-column-contacts
	 *   .tableview-customer-column-notes
	 *   .tableview-customer-column-notes-button
	 *   .tableview-customer-hbox
	 */

	@FXML
	private AnchorPane fxEquipment_AnchorPane;

	@FXML
	private GridPane fxEquipment_GridPane;

	@FXML
	private TableView<Equipment> fxEquipment_TableView;

	@FXML
	private TableColumn<Equipment,String> fxEquipment_TableCol_ID;


	@FXML
	private HBox fxEquipment_HBox;	// Bottom area container for buttons, search box, etc.

	/*
	 * TableView model.
	 */
	private final ObservableList<Equipment> cellDataObservable = FXCollections.observableArrayList();

	private final String LABEL_ID		= "Eq.-Nr.";
	private final String LABEL_NAME		= "Name";
	private final String LABEL_STATUS	= "Status";
	private final String LABEL_NOTES	= "Anmerk.";
	private final String LABEL_MAINTAIN	= "Wartung";

	private SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yyyy" );

	@Override
	public void inject( ControllerIntf dep ) {
		this.DS = (EquipmentDataIntf)dep;
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
		// Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
		fxEquipment_HBox.prefWidthProperty().bind( ((AnchorPane) fxEquipment_AnchorPane).widthProperty().subtract( 12 ) );
		fxEquipment_HBox.prefHeightProperty().bind( ((AnchorPane) fxEquipment_AnchorPane).heightProperty() );

		fxEquipment_GridPane.prefWidthProperty().bind( ((AnchorPane) fxEquipment_AnchorPane).widthProperty().subtract( 16 ) );
		fxEquipment_GridPane.prefHeightProperty().bind( ((AnchorPane) fxEquipment_AnchorPane).heightProperty().subtract( 70 ) );

		/*
		 * Bottom area HBox extends from the top across the entire AnchorPane hiding
		 * GridPane/TableView underneath (depending on z-stacking order). This prevents
		 * Mouse events from being propagated to TableView.
		 * 
		 * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
		 * to the underlying GridPane/TableView layer (Mouse event "transparency").
		 */
		fxEquipment_HBox.setPickOnBounds( false );

		/*
		 * Visualize resizing propagation by colored bounding boxes.
		 */
		//fxEquipment_GridPane.setStyle( "-fx-border-color: red;" );
		//fxEquipment_HBox.setStyle( "-fx-border-color: blue;" );

		fxEquipment_HBox.getStyleClass().add( "tableview-equipment-hbox" );


		/*
		 * Construct TableView columns.
		 * 
		 * TableView presents a row/column cell rendering of an ObservableList<Object>
		 * model. Each cell computes a "value" from the associated object property that
		 * defines how the object property is visualized in a TableView.
		 * See also: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
		 * 
		 * TableView columns define how object properties are visualized and cell values
		 * are computed.
		 * 
		 * In the simplest form, cell values are bound to object properties, which are
		 * public getter-names of the object class, and visualized in a cell as text.
		 * 
		 * More complex renderings such as with graphical elements, e.g. buttons in cells,
		 * require overloading of the built-in behavior in:
		 *   - CellValueFactory - used for simple object property binding.
		 *   - CellFactory - overriding methods allows defining complex cell renderings. 
		 * 
		 * Constructing a TableView means defining
		 *   - a ObservableList<Object> model
		 *   - columns with name, css-style and Cell/ValueFactory.
		 * 
		 * Variation 1: Initialize columns defined in FXML.
		 *  - Step 1: associate a .css class with column.
		 *  - Step 2: bind cell value to object property (must have public property getters,
		 *            getId(), getName()).
		 */
		fxEquipment_TableCol_ID.getStyleClass().add( "tableview-equipment-column-id" );
		fxEquipment_TableCol_ID.setText( LABEL_ID );
		fxEquipment_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "id" ) );

		/*
		 * Variation 2: Programmatically construct TableView columns.
		 */
		TableColumn<Equipment,String> tableCol_NAME = new TableColumn<>( LABEL_NAME );
		tableCol_NAME.getStyleClass().add( "tableview-equipment-column-name" );
		tableCol_NAME.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Equipment c = cellData.getValue();
			observable.set( c.getName() );
			return observable;
		});

		TableColumn<Equipment,String> tableCol_STATUS = new TableColumn<>( LABEL_STATUS );
		tableCol_STATUS.getStyleClass().add( "tableview-equipment-column-status" );
		tableCol_STATUS.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			// Render status as 3-letter shortcut of Equipment state enum.
			Equipment c = cellData.getValue();
			observable.set( c.getStatus().name().substring( 0, 3 ) );
			return observable;
		});

		TableColumn<Equipment,String> tableCol_MAINTAIN = new TableColumn<>( LABEL_MAINTAIN );
		tableCol_MAINTAIN.getStyleClass().add( "tableview-equipment-column-contacts" );

		tableCol_MAINTAIN.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Equipment c = cellData.getValue();
			Date date = c.getMaintenanceDate();
			String show = "-";
			if( date != null ) {
				show = dateFormat.format(date);
			}
			observable.set( show );
			return observable;
		});

		// TableColumn<Equipment,String> tableCol_NOTES = new TableColumn<>( "Notes" );
		TableColumn<Equipment,String> tableCol_NOTES = new TableColumn<>( LABEL_NOTES );
		tableCol_NOTES.getStyleClass().add( "tableview-equipment-column-notes" );

		tableCol_NOTES.setCellFactory(

			// Complex rendering of Notes column as clickable button with number of notes indicator.
			new Callback<TableColumn<Equipment,String>, TableCell<Equipment, String>>() {

				@Override
				public TableCell<Equipment, String> call( TableColumn<Equipment, String> col ) {

					col.setCellValueFactory( cellData -> {
						Equipment c = cellData.getValue();
						StringProperty observable = new SimpleStringProperty();
						observable.set( c.getId() );
						return observable;
					});

					TableCell<Equipment, String> tc = new TableCell<Equipment, String>() {
						final Button btn = new Button();

						@Override public void updateItem( final String item, final boolean empty ) {
							super.updateItem( item, empty );
							int rowIdx = getIndex();
							ObservableList<Equipment> cust = fxEquipment_TableView.getItems();

							if( rowIdx >= 0 && rowIdx < cust.size() ) {
								Equipment equipment = cust.get( rowIdx );
								setGraphic( null );		// always clear, needed for refresh
								if( equipment != null ) {
									btn.getStyleClass().add( "tableview-equipment-column-notes-button" );
									List<LogEntry> nL = equipment.getNotes();
									btn.setText( "notes: " + nL.size() );
									setGraphic( btn );	// set button as rendering of cell value
	
									//Event updateEvent = new ActionEvent();
									btn.setOnMouseClicked( event -> {
										String n = equipment.getName();
										String label = ( n==null || n.length()==0 )? equipment.getId() : n;
	
										PopupNotes popupNotes = new PopupNotes( label, nL );
	
										popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
											// Notification that List<Note> has been updated.
											// update button label [note: <count>]
											btn.setText( "notes: " + equipment.getNotes().size() );
											// -> save node
											DS.updateEquipment( equipment );
										});
	
										popupNotes.show();
									});
								}
							} else {
								//System.out.println( "OutOfBounds rowIdx() ==> " + rowIdx );
								setGraphic( null );		// reset button in other rows
							}
						}
					};
					return tc;
				}
			});

		// Add programmatically generated columns to TableView. Columns appear in order.
		fxEquipment_TableView.getColumns().clear();
		fxEquipment_TableView.getColumns().addAll( Arrays.asList(
			fxEquipment_TableCol_ID,
			tableCol_STATUS,
			tableCol_NOTES,
			tableCol_NAME,
			tableCol_MAINTAIN
		));

		/*
		 * Define selection model that allows to select multiple rows.
		 */
		fxEquipment_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

		/*
		 * Allow horizontal column squeeze of TableView columns. Column width can be fixed
		 * with -fx-pref-width: 80px;
		 */
		fxEquipment_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


		/*
		 * Double-click on row: open update dialog.
		 */
		fxEquipment_TableView.setRowFactory( tv -> {
			TableRow<Equipment> row = new TableRow<>();
			row.setOnMouseClicked( event -> {
				if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
					// Equipment rowData = row.getItem();
					// fxEquipment_TableView.getSelectionModel().select( row.getIndex() );
					//table.getSelectionModel().select( Math.min( i, size - 1 ) );
					fxEquipment_Update();
				}
			});
			return row;
		});

		/*
		 * Load objects into TableView model.
		 */
		fxEquipment_TableView.getItems().clear();
		Collection<Equipment> col = DS.findAllEquipments();
		if( col != null ) {
			cellDataObservable.addAll( col );
		}
		fxEquipment_TableView.setItems( cellDataObservable );
	}

	@Override
	public void stop() {
	}


	@FXML
	void fxEquipment_Delete() {
		ObservableList<Equipment> selection = fxEquipment_TableView.getSelectionModel().getSelectedItems();
		List<Equipment> toDel = new ArrayList<Equipment>();
		List<String> ids = new ArrayList<String>();
		for( Equipment c : selection ) {
			toDel.add( c );
		}
		fxEquipment_TableView.getSelectionModel().clearSelection();
		for( Equipment c : toDel ) {
			ids.add( c.getId() );
			// should not alter cellDataObservable while iterating over selection
			cellDataObservable.remove( c );
		}
		DS.deleteEquipment( ids );
	}

	@FXML
	void fxEquipment_New() {
		Equipment equipment = DS.newEquipment( null );
		openUpdateDialog( equipment, true );
	}

	@FXML
	void fxEquipment_Update() {
		Equipment equipment = fxEquipment_TableView.getSelectionModel().getSelectedItem();
		if( equipment != null ) {
			openUpdateDialog( equipment, false );
		//} else {
		//	System.err.println( "nothing selected." );
		}
	}

	@FXML
	void fxEquipment_Exit() {
		App.getInstance().stop();
	}


	/*
	 * Private helper methods.
	 */
	private final String SEP = ";";		// separates contacts in externalized String

	private void openUpdateDialog( Equipment c, boolean newItem ) {
		List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
		String n = c.getName();
		String label = ( n==null || n.length()==0 )? c.getId() : n;

        String maintain = " - ";
        if (c.getMaintenanceDate() != null) {
            maintain = c.getMaintenanceDate().toString();
        }

		PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
			new StringTestUpdateProperty( LABEL_ID, c.getId(), false ),
			new StringTestUpdateProperty( LABEL_NAME, c.getName(), true ),
			new StringTestUpdateProperty( LABEL_STATUS, c.getStatus().name(), true ),
			new StringTestUpdateProperty( LABEL_MAINTAIN, maintain, true )
		));

		// called when "OK" button in EntityEntryDialog is pressed
		dialog.addEventHandler( ActionEvent.ACTION, event -> {
			updateObject( c, altered, newItem );
		});

		dialog.show();
	}

	private void updateObject( Equipment equipment, List<StringTestUpdateProperty> altered, boolean newItem ) {
		for( StringTestUpdateProperty dp : altered ) {
			String pName = dp.getName();
			String alteredValue = dp.getValue();
			//System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

			if( pName.equals( LABEL_NAME ) ) {
				equipment.setName( alteredValue );
			}
			if( pName.equals( LABEL_STATUS ) ) {
				String av = alteredValue.toUpperCase();
				if( av.startsWith( "VOR" ) ) {
					equipment.setStatus( EquipmentStatus.VERFUEGBAR );
				}
				if( av.startsWith( "VER" ) ) {
					equipment.setStatus( EquipmentStatus.VERLIEHEN );
				}
				if( av.startsWith( "DEF" ) ) {
					equipment.setStatus( EquipmentStatus.DEFEKT );
				}
				if( av.startsWith( "REP" ) ) {
					equipment.setStatus( EquipmentStatus.IN_REPERATUR );
				}
				if( av.startsWith( "WAR" ) ) {
					equipment.setStatus( EquipmentStatus.IN_WARTUNG );
				}
				if( av.startsWith( "MWA" ) ) {
					equipment.setStatus( EquipmentStatus.MUSS_WARTUNG );
				}
			}
			if( pName.equals( LABEL_MAINTAIN ) ) {
                try {
                    Date date = dateFormat.parse(alteredValue);
                    equipment.setMaintenanceDate(date);
                } catch (ParseException e) {
                    System.out.println("Datumsformat nicht erkannt!");
                    //e.printStackTrace();
                }
			}
		}
		if( altered.size() > 0 ) {
			DS.updateEquipment( equipment );	// update object in persistent store
			if( newItem ) {
				int last = cellDataObservable.size();
				cellDataObservable.add( last, equipment );
			}
			// refresh TableView (trigger update
			fxEquipment_TableView.getColumns().get(0).setVisible(false);
			fxEquipment_TableView.getColumns().get(0).setVisible(true);

			altered.clear();	// prevent double save if multiple events fire
		}
	}
}
