package com.businessapp;

import com.businessapp.fxgui.AppController;
import com.businessapp.fxgui.CalculatorViewController;
import com.businessapp.fxgui.BusinessCustomerViewController;
import com.businessapp.fxgui.IndividualCustomerViewController;
import com.businessapp.fxgui.ReservationViewController;
import com.businessapp.logic.BusinessLogicIntf;
import com.businessapp.logic.LogicFactory;
import com.businessapp.logic.LogicIntf;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class App extends Application {
	public final static String NAME = "SE Business App";
	private Scene rootScene;
	private BorderPane rootPane;
	private TabPane tabsPane;
	private FXSceneLoader sceneLoader;


	public static void main( String[] args) {
		launch( args );
	}


	@Override
	public void start( Stage stage ) {
		int i = 1;
		sceneLoader = new FXSceneLoader( this );

		rootPane = (BorderPane)sceneLoader.loadFXML( AppController.class, ( controller, fxmlRoot ) -> {
			this.rootScene = new Scene( (BorderPane)fxmlRoot, 800, 480);
		});
		tabsPane = (TabPane)rootPane.lookup( "#tabs-pane" );

		//LogicFactory.init();
		BusinessLogicIntf businessLogic = LogicFactory.getBusinessLogic();
		businessLogic.loadData();

		Tab t0 = loadTaxCalculatorView( "MwSt-Rechner", i++, LogicFactory.getCalculatorLogic() );
		//Tab t1 = loadTaxCalculatorView( "Tax-Calculator (Demo)", i++, new demo.CalculatorDemo() );

		Tab t2 = addTab( "Privatkunden", i++, sceneLoader.loadFXML(
				IndividualCustomerViewController.class, ( controller, rootNode ) -> {
					controller.injectImpl( (LogicIntf)businessLogic );
				}
		));
		Tab t3 = addTab( "Firmenkunden", i++, sceneLoader.loadFXML(
				BusinessCustomerViewController.class, ( controller, rootNode ) -> {
					controller.injectImpl( (LogicIntf)businessLogic );
				}
		));
		Tab t4 =  addTab( "Reservierungen", i++, sceneLoader.loadFXML(
				ReservationViewController.class, ( controller, rootNode ) -> {
					controller.injectImpl( (LogicIntf)businessLogic );
				}
		));

		tabsPane.getSelectionModel().select( t0 );

		addTab( "Rechnungen", i++, null );
		addTab( "Fahrzeuge", i++, null );
		addTab( "Personal", i++, null );

		stage.setTitle( App.NAME );
		stage.setScene( this.rootScene );
		stage.show();
	}

	Scene getRootScene() {
		return this.rootScene;
	}


	/*
	 * ************************************************************************
	 * Private method(s).
	 */
	private Tab loadTaxCalculatorView( String name, int n, LogicIntf logicImpl ) {
		Node fxmlRoot = sceneLoader.loadFXML( CalculatorViewController.class, ( controller, rootNode ) -> {
			controller.injectImpl( logicImpl );
		});
		Tab tab = addTab( name, n, fxmlRoot );
		tabsPane.addEventFilter( MouseEvent.MOUSE_RELEASED, ( e ) -> {
			Tab t = (Tab)tabsPane.getSelectionModel().selectedItemProperty().get();
			if( t==tab ) {
				fxmlRoot.requestFocus();
			}
		});
		return tab;
	}

	private Tab addTab( String name, int n, Node loadedFXMLRoot ) {
		Tab tab = new Tab( name );
		tabsPane.getTabs().add( n, tab );
		if( loadedFXMLRoot != null ) {
			// bind size of loaded FXMLRoot node to size of top-level rootPane.
			((AnchorPane)loadedFXMLRoot).prefWidthProperty().bind( rootPane.widthProperty());
			((AnchorPane)loadedFXMLRoot).prefHeightProperty().bind( rootPane.heightProperty());
			tab.setContent( loadedFXMLRoot );
		}
		return tab;
	}

}
