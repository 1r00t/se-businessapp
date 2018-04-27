package com.businessapp;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * ComponentBuilder is home and manager of all components.
 *
 */
class ComponentBuilder {

	private List<Component> components;


	@FunctionalInterface
	interface ComponentCallbackIntf {
		void call( ControllerIntf controller );
	}

	/**
	 * Constructor
	 * @param components list of components
	 */
	ComponentBuilder( List<Component> components ) {
		this.components = components;
	}

	/**
	 * Return list of components
	 * @return list of components
	 */
	List<Component>getComponents() {
		return components;
	}

	/**
	 * Start all components in list order. Logic first then GUI controller.
	 * @param logMsg message to log start of a controller
	 * @param logLevel 0: no log, 1: logic controller, 2: GUI controller, 3: both
	 * @param callback to run code with controller in calling function.
	 */
	void start( String logMsg, int logLevel, ComponentCallbackIntf callback ) {
		boolean first = true;
		log( logMsg, logLevel );
		for( Component c : components ) {
			if( c.getLogic() != null ) {
				callback.call( c.getLogic() );
				first = log( first, ( ( logLevel & 1 ) == 0 )? null : c.getLogic() );
			}
		}
		for( Component c : components ) {
			if( c.getGUIController() != null ) {
				callback.call( c.getGUIController() );
				first = log( first, ( ( logLevel & 2 ) == 0 )? null: c.getGUIController() );
			}
		}
		log( " - OK.\n", logLevel );
	}

	/**
	 * Start all components in reverse list order. GUI controller first then logic.
	 * @param logMsg message to log stopping a controller
	 * @param logLevel logLevel 0: no log, 1: logic controller, 2: GUI controller, 3: both
	 * @param callback to run code with controller in calling function.
	 */
	void stop( String logMsg, int logLevel, ComponentCallbackIntf callback ) {
		boolean first = true;
		Collections.reverse( components );
		log( logMsg, logLevel );
		for( Component c : components ) {
			callback.call( c.getGUIController() );
			first = log( first, ( ( logLevel & 2 ) == 0 )? null: c.getGUIController() );
		}
		for( Component c : components ) {
			callback.call( c.getLogic() );
			first = log( first, ( ( logLevel & 1 ) == 0 )? null : c.getLogic() );
		}
	}


	/*
	 * Private helper methods.
	 */
	private boolean log( boolean first, ControllerIntf controller ) {
		boolean res = first;
		if( controller != null ) {
			res = false;
			String comma = first? "" : ", ";
			System.out.print( comma + controller.getClass().getSimpleName() );
			annimateDelay( 80 );
		}
		return res;
	}

	private void log( String logLine, int logLevel ) {
		if( logLevel > 0 ) {
			annimateDelay( 120 );
			System.out.print( logLine );
		}
	}

	private void annimateDelay( int millis ) {
		try { TimeUnit.MILLISECONDS.sleep( millis ); } catch( InterruptedException e ) { }
	}

}
