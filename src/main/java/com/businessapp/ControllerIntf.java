package com.businessapp;


/**
 * Public controller interface with basic lifecycle operations and
 * injection of controller dependencies.
 *
 */
public interface ControllerIntf {

	/** 
	 * Dependency injection is a principle that allows that references of dependents
	 * are injected from outside rather than hard coded.
	 * 
	 * Inject reference to another controller to interact with.
	 * @param dep reference to another controller.
	 */
	public void inject( ControllerIntf dep );

	/**
	 * @param parent component of which a controller is part of.
	 */
	public void inject( Component parent );

	/**
	 * Start controller.
	 */
	public void start();

	/**
	 * Stop controller.
	 */
	public void stop();

}
