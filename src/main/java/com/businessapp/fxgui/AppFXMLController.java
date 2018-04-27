package com.businessapp.fxgui;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;


public class AppFXMLController implements FXMLControllerIntf {

	@FXML
	private TabPane fxApp_TabsPane;

	@Override
	public void inject(ControllerIntf dep) {
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}


	@FXML
	private void exitButton() {
		App.getInstance().stop();
	}

	public TabPane getTabPane() {
		return fxApp_TabsPane;
	}

}
