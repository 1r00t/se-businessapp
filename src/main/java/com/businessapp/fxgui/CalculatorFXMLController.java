package com.businessapp.fxgui;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.CalculatorLogicIntf;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class CalculatorFXMLController implements CalculatorGUI_Intf {
	private CalculatorLogicIntf logic = null;

	private final Object[][] ShortCutKeys = new Object[][] {
		{ "\r",	Token.K_EQ		},
		{ "\b",	Token.K_BACK	},
		{ "c",	Token.K_C		},
		{ "e",	Token.K_CE		},
		{ ".",	Token.K_DOT		},
		{ "m",	Token.K_VAT		},
		{ "t",	Token.K_1000	},
	};

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private TextField displayTextField;

	@FXML
	private TextArea sideTextArea;

	@FXML
	private GridPane keypadGridPane;


	@Override
	public void inject( ControllerIntf dep ) {
		this.logic = (CalculatorLogicIntf)dep;
	}

	@Override
	public void inject( Component parent ) {		
	}


	@Override
	public void start() {
		Tab tab = AppGUIBuilder.getInstance().getTab( this );
		TabPane tabsPane = tab.getTabPane();

		// add KEY_TYPED event handler when Calculator tab is selected, and remove if unselected
		tabsPane.getSelectionModel().selectedItemProperty().addListener( ( ov, oldTab, newTab ) -> {
			if( tab.getId().equals( newTab.getId() ) ) {
				//pane.setOnKeyTyped( e -> {
				//});
				tabsPane.addEventHandler( KeyEvent.KEY_TYPED, KEY_TYPED_EventHandler );

			} else {
				if( tab.getId().equals( oldTab.getId() ) ) {
					tabsPane.removeEventHandler( KeyEvent.KEY_TYPED, KEY_TYPED_EventHandler );
				}
			}
		});

		int keyPadCols = 4;	// -- Java 9: keypadGridPane.getColumnCount();
		int i = 0;
		for( Node n : keypadGridPane.getChildren() ) {
			Button btn = (Button)n;

			btn.setOnMousePressed( ( e ) -> {
				Button bt = (Button)e.getSource();
				int row = GridPane.getRowIndex( bt );
				int col = GridPane.getColumnIndex( bt );
				int idx = row * keyPadCols + col;	// flatten grid coordinates to idx[0..n]
				if( this.logic != null ) {
					this.logic.nextToken( Token.values()[ idx ] );
				}
			});

			// button has focus after valid KeyPress-event, button then also receives KeyRelease
			// to release focus (and remove border highlighting from button)
			btn.setOnKeyReleased( ( e ) -> {
				anchorPane.requestFocus();
			});

			btn.setText( KeyLabels[ i++ ] );
		}

		// regain focus of anchorPane element to receive key events.
		anchorPane.addEventFilter( MouseEvent.ANY, (e) -> anchorPane.requestFocus() );

		this.displayTextField.setEditable( false );
		this.sideTextArea.setEditable( false );
	}

	@Override
	public void stop() {
	}

	@Override
	public void writeTextArea(String text) {
		displayTextField.setText( text );
	}

	@Override
	public void writeSideArea(String text) {
		sideTextArea.setText( text );
	}


	/**
	 * Private methods.
	 */
	private EventHandler<KeyEvent> KEY_TYPED_EventHandler = new EventHandler<KeyEvent>() {
		public void handle( KeyEvent e ) {
			String s = e.getCharacter();	// e.getCode() -> case UP, DOWN, SHIFT...
			//System.err.print( e.getCharacter() );
			int idx = -1;
			for( int j=0; idx < 0 && j < KeyLabels.length; j++ ) {
				if( KeyLabels[ j ].equals( s ) ) {
					idx = j;
				}
			}
			if( idx < 0 ) {
				for( Object[] sc : ShortCutKeys ) {
					if( s.equals( sc[0] ) ) {
						idx = ((Token)sc[1]).ordinal();
						break;
					}
				}
			}
			if( idx >= 0 ) {
				Button btn = (Button)keypadGridPane.getChildren().get( idx );
				btn.requestFocus();	// mimic mouse pressed highlighting
				btn.fireEvent(
					new MouseEvent(MouseEvent.MOUSE_PRESSED,
						btn.getLayoutX(), btn.getLayoutY(),
						btn.getLayoutX(), btn.getLayoutY(), MouseButton.PRIMARY, 1,
						true, true, true, true, true, true, true, true, true, true, null
				));
			}
		}
	};

}
