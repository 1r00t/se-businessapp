package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.fxgui.CalculatorGUI_Intf.Token;


/**
 * Public interface of Calculator logic.
 */
public interface CalculatorLogicIntf extends ControllerIntf {

	/**
	 * Factory method that returns a new calculator logic controller instance.
	 * @return new instance of CalculatorLogic.
	 */
	public static CalculatorLogicIntf getController() {
		return new CalculatorLogic();
	}

	/**
	 * Method invoked by user interface to pass next token when user presses a the
	 * key pad.
	 * @param tok Token of the pressed key/button on the calculators input panel.
	 */
	public void nextToken( Token tok );

}
