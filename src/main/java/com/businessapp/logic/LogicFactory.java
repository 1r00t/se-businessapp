package com.businessapp.logic;


/**
 * Public singleton factory to create and provide access to singleton
 * implementation instances of LogicIntf interfaces.
 * <p>
 * Current instances created and obtained from LogicFactory:
 *	- CalculatorLogic,
 *	- BusinessLogic.
 *
 */
public class LogicFactory {
	private static LogicFactory singleton = new LogicFactory();

	private LogicIntf calculatorImpl;
	private BusinessLogicIntf businessLogicImpl;


	/*
	 * Private constructor.
	 */
	private LogicFactory() {
		this.calculatorImpl = new CalculatorLogic();
		this.businessLogicImpl = new BusinessLogic();
	}

	/**
	 * Public static access method to CalculatorLogic implementation.
	 * <p>
	 * @return reference to CalculatorLogic instance.
	 */
	public static final LogicIntf getCalculatorLogic() {
		return singleton.calculatorImpl;
	}

	/**
	 * Public static access method to BusinessLogic implementation.
	 * <p>
	 * @return reference to BusinessLogic instance.
	 */
	public static BusinessLogicIntf getBusinessLogic() {
		return singleton.businessLogicImpl;
	}

}
