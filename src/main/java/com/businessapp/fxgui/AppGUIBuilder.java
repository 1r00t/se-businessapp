package com.businessapp.fxgui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.businessapp.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class AppGUIBuilder {
	private static final AppGUIBuilder _appGuiBuilder = new AppGUIBuilder();

	private final HashMap<FXMLControllerIntf,Tab>_tabMap = new HashMap<FXMLControllerIntf,Tab>();
	private Scene scene = null;
	private TabPane tabsPane = null;


	@FunctionalInterface
	public interface AppGUIBuilderCallbackIntf {
		void call( boolean first, Component component, FXMLControllerIntf fxmlController );
	}

	private AppGUIBuilder() {
	}

	public static AppGUIBuilder getInstance() {
		return _appGuiBuilder;
	}

	public Tab getTab( FXMLControllerIntf controller ) {
		return _tabMap.get( controller );
	}

	public TabPane getTabPane() {
		return tabsPane;
	}


	public void buildAppUI( Stage stage, List<Component> components,
			AppGUIBuilderCallbackIntf initializer )
	{
		for( Component comp : components ) {
			scene = stage.getScene();
			Parent root = scene==null? null : scene.getRoot();
			String fxmlRes = comp.getFxmlResource();
			String name = comp.getName();
			try {
				URL url = AppFXMLController.class.getResource( fxmlRes );
				if( url != null ) {
					FXMLLoader loader = new FXMLLoader( url );
					Pane pane = loader.load();
					FXMLControllerIntf fxmlController = loader.<FXMLControllerIntf>getController();

					if( tabsPane == null ) {	// first pass
						root = pane;
						scene = new Scene( pane );
						stage.setScene( scene );
						if( fxmlController instanceof AppFXMLController ) {
							tabsPane = ((AppFXMLController)fxmlController).getTabPane();
						}
						/*
						 * Restrict max/min window expansion from CSS .root{ } rule.
						 * -- Java 9:
						 * /
						CssParser parser = new CssParser();
						Stylesheet css = parser.parse( this.getClass().getResource( cssPath ) );
						final Rule rootRules = css.getRules().get(0);
						* /

						Object val = null;
						if( ( val = fetchCssPropertyValue( rootRules, "-fx-max-width" ) ) != null ) {
							stage.setMaxWidth( (double)val );
						}
						if( ( val = fetchCssPropertyValue( rootRules, "-fx-min-width" ) ) != null ) {
							stage.setMinWidth( (double)val );
						}
						if( ( val = fetchCssPropertyValue( rootRules, "-fx-min-height" ) ) != null ) {
							stage.setMinHeight( (double)val );
						}
						*/
						stage.setMaxWidth( 1200.0 );
						stage.setMinWidth( 520.0 );
						stage.setMinHeight( 420.0 );

						initializer.call( true, comp, fxmlController );

					} else {
				        Tab tab = new Tab( name );
						// Make tab id unique.
						String tabId = name;
						for( Tab t : tabsPane.getTabs() ) {
							if( t.getId().equals( tabId ) ) {
								tabId += "A";
							}
						}
						tab.setId( tabId );
						tabsPane.getTabs().add( tab );

						initializer.call( false, comp, fxmlController );

						// bind size of loaded FXMLRoot node to size of top-level rootPane.
						pane.prefWidthProperty().bind( ((BorderPane) root).widthProperty());
						pane.prefHeightProperty().bind( ((BorderPane) root).heightProperty());

						tab.setContent( pane );
						_tabMap.put( fxmlController, tab );
					}
				}

				/*
				 * If fxmlRes.css exists, add .css stylesheet to root node. 
				 */
				String cssPath = fxmlRes.replace( ".fxml", ".css" );
				if( ( url = AppFXMLController.class.getResource( cssPath ) ) != null ) {
					root.getStylesheets().add( url.toExternalForm() );
				}

			} catch( IOException e ) {
				System.err.println( "IOException loading resource. " + comp.getName() + ", " + e.getMessage() );
			}
		}
	}

	/*
	 * Private methods.
	 * -- Java 9:
	 * /
	private Object fetchCssPropertyValue( Rule cssRule, String propertyName ) {
		try {
			return cssRule.getDeclarations().stream()
				.filter( d -> d.getProperty().equals( propertyName ) )
				.findFirst()
				.map( d -> d.getParsedValue().convert( null ) )
				.orElseThrow( () -> new Exception( "No CSS declaration found." ) );
	
		} catch( Exception e ) {
		}
		return null;
	}
	*/

}
