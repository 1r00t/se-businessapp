package com.businessapp.fxgui;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * Local helper class for a Popup that allows editing generic String property sets.
 * It is used when logic data, e.g. Customer data is updated or new objects are
 * created and property values need to be entered.
 */
class PopupUpdateProperties extends Stage {
	private final Scene scene;

	PopupUpdateProperties( String label, List<StringTestUpdateProperty> alteredProps, List<StringTestUpdateProperty> props ) {
		this.setTitle( label );
		this.initModality( Modality.WINDOW_MODAL );
		this.initStyle( StageStyle.UTILITY );
		this.setWidth( 360 );
		Node focus = null;

		TextField[] textFields = new TextField[ props.size() ];
		GridPane gp = new GridPane();
		for( int i=0; i < props.size(); i++ ) {
			StringTestUpdateProperty dp = props.get( i );
			textFields[ i ] = new TextField();
			gp.addRow( i, new Label( dp.getName() ), textFields[ i ] );
			gp.setHgap( 10 );
			gp.setVgap( 10 );
			GridPane.setHgrow( textFields[ i ], Priority.ALWAYS );

			textFields[ i ].setText( dp.getValueEmptyIfNull() );
			if( ! dp.isEditable() ) {
				textFields[ i ].setEditable( false );
				textFields[ i ].setStyle(
					"-fx-border-radius: 4px;" +
					"-fx-border-color: lightgrey;" +
					"-fx-background-color: #fafafa;"
				);
			} else {
				if( focus==null ) {
					focus = textFields[ i ];
				}
			}
		}
		Button OK = new Button( "OK" );
		OK.setDefaultButton( true );

		Button Cancel = new Button( "Cancel" );
		Cancel.setCancelButton( true );

		OK.setOnAction( e -> {
			for( int i=0; i < props.size(); i++ ) {
				StringTestUpdateProperty dp = props.get( i );
				String tf = textFields[ i ].getText();
				if( dp.testAndUpdate( tf ) ) {
					alteredProps.add( dp );
				}
			}
			// propagate ActionEvent back to Dialog object.
			//this.fireEvent( new javafx.event.ActionEvent() );
			this.close();	// close fires ACTION event
		});

		Cancel.setOnAction( e -> {
			this.close();
		});

		HBox buttons = new HBox();
		buttons.setSpacing( 10 );
		buttons.setAlignment( Pos.CENTER_RIGHT );
		buttons.getChildren().addAll( OK, Cancel );

		VBox layout = new VBox( 20 );
		VBox vspacer = new VBox( 20 );	// add more vertical space to fully show buttons
		layout.getChildren().addAll( gp, buttons, vspacer );
		layout.setPadding( new Insets( 5 ) );

		this.scene = new Scene( layout );
		this.setScene( this.scene );
		
		if( focus != null ) {
			focus.requestFocus();
		}
	}

}
