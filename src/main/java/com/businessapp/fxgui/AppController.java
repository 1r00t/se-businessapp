package com.businessapp.fxgui;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import com.businessapp.App;
import com.businessapp.logic.LogicIntf;


/**
 * UI Controller implementation that is associated with same name .fxml file.
 * <p>
 * AppController implements the controls of the Main tab.
 *
 */
public class AppController implements ViewControllerIntf {

	@FXML
	private BorderPane FXID_AppView;

	@FXML
	private void exit() {
		System.out.println( App.NAME + ".exit()" );
		System.exit( 0 );
	}

	@FXML
	private void initialize() {
	}

	/**
	 * Method to inject an anonymous implementation instance of the controller's
	 * functional (business) logic. Method is invoked during ViewController startup.
	 * <p>
	 * @param logicImpl anonymous implementation of the controller's functional (business) logic.
	 */
	@Override
	public void injectImpl( LogicIntf logicImpl ) {
		buildView();
	}

	/**
	 * Method that allows to implement specific UI-logic of a ViewController.
	 */
	@Override
	public void buildView() {
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

}
