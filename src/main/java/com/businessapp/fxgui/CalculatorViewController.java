package com.businessapp.fxgui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import com.businessapp.logic.CalculatorLogicIntf;
import com.businessapp.logic.CalculatorLogicIntf.Token;
import com.businessapp.logic.LogicIntf;


/**
 * UI Controller implementation that is associated with same name .fxml file.
 * <p>
 * CalculatorViewController implements the calculator view controls.
 *
 */
public class CalculatorViewController implements ViewControllerIntf {
	private int keypadGridPaneColumns = 0;
	private final Button[] keypadButtons = new Button[ CalculatorLogicIntf.KeyLabels.length ];
	private Button buttonWithFocus = null;
	private CalculatorLogicIntf calculator = null;

	@FXML
	private AnchorPane FXID_CalculatorView;

	@FXML
	private TextField displayTextField;

	@FXML
	private TextArea sideTextArea;

	@FXML
	private GridPane keypadGridPane;

	/**
	 * Method to inject an anonymous implementation instance of the controller's
	 * functional (business) logic. Method is invoked during ViewController startup.
	 * <p>
	 * @param logicImpl anonymous implementation of the controller's functional (business) logic.
	 */
	@Override
	public void injectImpl( LogicIntf logicImpl ) {
		this.calculator = (CalculatorLogicIntf)logicImpl;
		buildView();
	}

	/**
	 * Method that allows to implement specific UI-logic of a ViewController.
	 */
	@Override
	public void buildView() {
		this.displayTextField.setEditable( false );
		// 1st digit: min, 2nd digit max length. The string will be truncated if it is longer.
		//this.displayTextField.textProperty().bind( Bindings.format( "%1.16s", DISPLAY ) );
		this.displayTextField.textProperty().bind( CalculatorLogicIntf.DISPLAY );

		this.sideTextArea.setEditable( false );
		this.sideTextArea.textProperty().bind( CalculatorLogicIntf.SIDEAREA );
		CalculatorLogicIntf.SIDEAREA.set( "" );

		for( Node child : keypadGridPane.getChildren() ) {
			if( GridPane.getRowIndex( child ) == 0 ) {
				keypadGridPaneColumns = Math.max( keypadGridPaneColumns, GridPane.getColumnIndex( child ) + 1 );
			} else
				break;
		}
		for( Node child : keypadGridPane.getChildren() ) {
			if( child instanceof Button ) {
				Button bt = (Button)child;
				int row = GridPane.getRowIndex( child );
				int col = GridPane.getColumnIndex( child );
				int i = row * keypadGridPaneColumns + col;
				bt.setText( CalculatorLogicIntf.KeyLabels[i] );
				keypadButtons[ i ] = bt;
			}
			
		}
		FXID_CalculatorView.setOnKeyTyped( ( e ) -> {
			String s = e.getCharacter();
			int i = -1;
			for( int j=0; i < 0 && j < CalculatorLogicIntf.KeyLabels.length; j++ ) {
				if( CalculatorLogicIntf.KeyLabels[ j ].equals( s ))
					i = j;
			}
			if( i < 0 ) {
				for( Object[] sc : CalculatorLogicIntf.ShortCutKeys ) {
					if( s.equals( sc[0]) ) {
						i = ((Token)sc[1]).ordinal();
						break;
					}
				}
			}
			if( i >= 0 ) {
				Button button = keypadButtons[ i ];
				button.requestFocus();
				buttonWithFocus = button;
				button.fireEvent(
					new MouseEvent(MouseEvent.MOUSE_PRESSED,
					button.getLayoutX(), button.getLayoutY(),
					button.getLayoutX(), button.getLayoutY(), MouseButton.PRIMARY, 1,
					true, true, true, true, true, true, true, true, true, true, null
				));
			}
		});

		// needed to regain focus on anchorPane element to receive key events.
		FXID_CalculatorView.addEventFilter( MouseEvent.ANY, (e) -> FXID_CalculatorView.requestFocus());

		FXID_CalculatorView.setOnKeyReleased( ( e ) -> {
			if( buttonWithFocus != null && buttonWithFocus.isFocused() ) {
				buttonWithFocus.fireEvent( new MouseEvent(MouseEvent.MOUSE_RELEASED,
					buttonWithFocus.getLayoutX(), buttonWithFocus.getLayoutY(),
					buttonWithFocus.getLayoutX(), buttonWithFocus.getLayoutY(), MouseButton.PRIMARY, 1,
					true, true, true, true, true, true, true, true, true, true, null
				));
			}
			buttonWithFocus = null;
		});
	}

	/**
	 * Method that allows to refresh a UI-view, e.g. to synchronize the state of the
	 * view with the state in the underlying functional (business) logic.
	 * @return used to pass number (size) of refreshed entities to adjust selection in view accordingly.
	 */
	@Override
	public int refreshView() {
		return 0;
	}


	/*
	 * Private method(s).
	 */
	@FXML
	private void keyPadPressed( Event e ) {
		Button bt = (Button)e.getSource();
		int row = GridPane.getRowIndex( bt );
		int col = GridPane.getColumnIndex( bt );
		int i = row * keypadGridPaneColumns + col;
		if( calculator != null ) {
			calculator.nextToken( Token.values()[ i ] );
		}
	}

}
