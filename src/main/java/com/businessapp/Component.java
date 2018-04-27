package com.businessapp;

/**
 * Component is a higher-level system building block. A Component in this app has a:
 * - name,
 * - GUI/presentation created by JavaFX runtime from fxml resource (.fxml file) with a
 *   - Tab on the main Tab-panel with content from a .fxml resource,
 *   - FXML controller that handles user interaction and presentation events.
 * 
 * Furthermore, a component has:
 * - separate logic that is isolated from the GUI/presentation. 
 * 
 */
public class Component {

	/*
	 * Private ProxyController has no effect. The only purpose is to prevent 'null'
	 * references / NullpointerExceptions when no controller is present.
	 */
	private class ProxyController implements ControllerIntf {
		@Override
		public void inject( ControllerIntf dep ) { }

		@Override
		public void inject(Component parent) { }

		@Override
		public void start() { }

		@Override
		public void stop() { }
	}

	private final String name;				// component name
	private final ControllerIntf logic;		// logic controller part
	private final String fxmlResource;		// fxml resource loaded to create fxmlController
	private ControllerIntf fxmlController;	// GUI/fxml controller part

	/**
	 * Constructor.
	 * @param name component name
	 * @param fxmlResource to load FXML and create fxmlController
	 * @param logic controller provided
	 */
	Component( String name, String fxmlResource, ControllerIntf logic ) {
		this.name = name;
		this.fxmlResource = fxmlResource;
		this.fxmlController = new ProxyController();
		this.logic = logic != null? logic : new ProxyController();
		this.logic.inject( this );
	}

	/**
	 * Public getters.
	 */
	public String getName() {
		return name;
	}

	public String getFxmlResource() {
		return fxmlResource;
	}

	public ControllerIntf getGUIController() {
		return fxmlController;
	}

	public ControllerIntf getLogic() {
		return logic;
	}

	/**
	 * Injection of GUI controller created by JavaFX run time.
	 * @param fxmlController GUI controller.
	 */
	public void inject( ControllerIntf fxmlController ) {
		this.fxmlController = fxmlController != null? fxmlController : new ProxyController();
		// mutual exchange of references.
		this.logic.inject( fxmlController );
		this.fxmlController.inject( logic );
		this.fxmlController.inject( this );
	}

}
