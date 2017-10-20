package com.businessapp.fxgui;

import com.businessapp.logic.LogicIntf;


/**
 * Public interface all UI/FXML ViewControllers must implement.
 */
public interface ViewControllerIntf {

	/**
	 * Method to inject an anonymous implementation instance of the controller's
	 * functional (business) logic. Method is invoked during ViewController startup.
	 * <p>
	 * @param logicImpl anonymous implementation of the controller's functional (business) logic.
	 */
	void injectImpl( LogicIntf logicImpl );

	/**
	 * Method that allows to implement specific UI-logic of a ViewController.
	 */
	void buildView();

	/**
	 * Method that allows to refresh a UI-view, e.g. to synchronize the state of the
	 * view with the state in the underlying functional (business) logic.
	 * @return used to pass number (size) of refreshed entities to adjust selection in view accordingly.
	 */
	public int refreshView();

}
