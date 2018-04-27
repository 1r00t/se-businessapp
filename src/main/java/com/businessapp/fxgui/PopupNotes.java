package com.businessapp.fxgui;

import java.util.List;

import com.businessapp.pojos.LogEntry;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * Local helper class for a Popup that allows editing LogEntries.
 */
class PopupNotes extends Stage {
	private final Scene scene;
	private final ObservableList<LogEntry> cellDataObservable;


	public PopupNotes( String label, List<LogEntry> notes ) {
		final PopupNotes _this = this;
		this.setTitle( label );
		this.setWidth( 600 );
		this.setHeight( 500 );

		TableView<LogEntry> tableView = new TableView<LogEntry>();

		tableView.getSelectionModel().setCellSelectionEnabled( true );
		tableView.setEditable( true );

		cellDataObservable = FXCollections.observableArrayList( notes );


		/**
		 * Define column: Time.
		 */
		TableColumn<LogEntry,String> timeCol = new TableColumn<LogEntry,String>( "Zeit" );
		timeCol.setMinWidth( 110 );

		timeCol.setCellValueFactory(
			cellData2 -> {
				String cellVal = cellData2.getValue().getSimpleTimestamp();
				return new SimpleStringProperty( cellVal );
			}
		);

		/**
		 * Define column: Notes.
		 */
		TableColumn<LogEntry,String> noteCol = new TableColumn<LogEntry,String>( "Notizen" );
		noteCol.setMinWidth( 440 );
		noteCol.setEditable( true );

		noteCol.setCellFactory( TextFieldTableCell.forTableColumn() );

		noteCol.setCellValueFactory(
			cellData2 -> {
				String cellVal = cellData2.getValue().getLog();
				return new SimpleStringProperty( cellVal );
			}
		);

		noteCol.setOnEditCommit( edit -> {
			//int i = edit.getTablePosition().getRow();
			LogEntry cellValue = edit.getRowValue();
			String newVal = edit.getNewValue().trim();
			cellValue.setLog( newVal );		// update cell value
			updateNoteList( notes );		// update List<Note>
			// notify listener(s) of updated List<Note>
			_this.fireEvent( new ActionEvent() );
		});

		/*
		 * Double-click on row: open update dialog.
		 */
		tableView.setRowFactory( tv -> {
			TableRow<LogEntry> row = new TableRow<>();
			row.setOnMouseClicked( event -> {
				if( event.getClickCount() == 2 && ( row.isEmpty() ) ) {
					row.setEditable( true );
					LogEntry cdn = new LogEntry();
					cellDataObservable.add( cdn );
					updateNoteList( notes );	// clear + rebuild List<Note>
					row.requestFocus();
					_this.fireEvent( new ActionEvent() );
				}
			});
			return row;
		});

		tableView.getColumns().add( timeCol );
		tableView.getColumns().add( noteCol );

		tableView.setItems( cellDataObservable );

		VBox layout = new VBox( 20 );
		layout.getChildren().addAll( tableView );

		this.scene = new Scene( layout );
		this.setScene( this.scene );
	}


	/*
	 * Private helper methods.
	 * 
	 */
	private void updateNoteList( List<LogEntry> notes ) {
		// clear + rebuild List<Note>
		notes.clear();
		for( LogEntry cn : cellDataObservable ) {
			notes.add( cn );
		}
	}

}
