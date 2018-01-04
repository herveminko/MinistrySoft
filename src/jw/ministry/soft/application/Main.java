package jw.ministry.soft.application;

//import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

//import ch.makery.address.MainApp;
//import ch.makery.address.view.RootLayoutController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jw.ministry.soft.modules.gui.views.congregation.model.CongregationModel;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import fxmlviews.RootController;



/**
 * Main Application class.
 * @author Hervé Minko.
 *
 */
public class Main extends Application {

	public static  final String FX_VIEWS_DIRECTORY = "fxmlviews/";//"../modules/gui/views/";
	public static  final String BUNDLES_DIRECTORY = "bundles/";//"../modules/gui/views/";

	private Stage primaryStage;
	private BorderPane rootLayout;

	private CongregationModel currentCongregation;

	final TextField username = new TextField();
	final PasswordField password = new PasswordField();
	final Action actionLogin = new AbstractAction("Login") {
	    // This method is called when the login button is clicked ...
	    public void handle(ActionEvent ae) {
	        Dialog d = (Dialog) ae.getSource();
	        // Do the login here.
	        if (username.getText().equals("admin") && password.getText().equals("admin")) {
		        d.hide();
	        } else {
	        	String appVersion = "";
	        	ResourceBundle bundle;
				try {
					bundle = Main.getMainBundle();
		        	appVersion = bundle.getString("app_version");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	    		Dialogs.create()
		        .title("MinistrySoftApp " + appVersion)
		        .masthead("Erreur d'authentification")
		        .message("L'authentification n'a as fonctionné. Veuillez rééssayer.")
		        .showWarning();
	        }
	    }
	};

	final Action actionCancel = new AbstractAction("Annuler") {
	    // This method is called when the cancel button is clicked ...
	    public void handle(ActionEvent ae) {
	    	System.exit(0);
	    }
	};


	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			HibernateUtil.getSessionFactory();
			this.primaryStage = primaryStage;

        	String appVersion = "";
        	ResourceBundle bundle;
			try {
				bundle = Main.getMainBundle();
	        	appVersion = bundle.getString("app_version");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.primaryStage.setTitle("MinistrySoftApp " + appVersion);

			// Set the application icon.
			this.primaryStage.getIcons().add(
					new Image("file:resources/images/books.jpg"));

			initRootLayout();
			primaryStage.setMaximized(true);

			primaryStage.show();

			//openLoginDialog(this.primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}


	/**
	 * Open the login dialog.
	 * @param stage
	 */
	public void openLoginDialog(Stage stage) {
		   // Create the custom dialog.
	    Dialog dlg = new Dialog(stage, "Login Dialog", false);

	    GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 0, 10));

	    username.setPromptText("Username");
	    password.setPromptText("Password");

	    grid.add(new Label("Username:"), 0, 0);
	    grid.add(username, 1, 0);
	    grid.add(new Label("Password:"), 0, 1);
	    grid.add(password, 1, 1);

	    ButtonBar.setType(actionLogin, ButtonType.OK_DONE);
	    ButtonBar.setType(actionCancel, ButtonType.CANCEL_CLOSE);
	    actionLogin.disabledProperty().set(true);

	    // Do some validation (using the Java 8 lambda syntax).
	    username.textProperty().addListener((observable, oldValue, newValue) -> {
	        actionLogin.disabledProperty().set(newValue.trim().isEmpty());
	    });

	    dlg.setMasthead("Look, a Custom Login Dialog");
	    dlg.setContent(grid);
	    dlg.getActions().addAll(actionLogin,actionCancel);


	    // Request focus on the username field by default.
	    Platform.runLater(() -> username.requestFocus());

	    dlg.setClosable(false);
	    dlg.show();
	}


	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {

			// Load root layout from fxml file.
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			FXMLLoader loader = new FXMLLoader();
			ResourceBundle resources = getMainBundle();
			URL fxmlURL = classLoader.getResource(FX_VIEWS_DIRECTORY + "Root.fxml");
			loader.setLocation(fxmlURL);
			loader.setResources(resources);
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent t) {
	            	HibernateUtil.shutdown();
	            	Platform.exit();
	                System.exit(0);
	            }
	        });


			// Give the controller access to the main app.
			//RootLayoutController controller = loader.getController();
			RootController controller = loader.getController();
//			controller.setMainApp(this);
			controller.setFxStage(primaryStage);

			primaryStage.show();

			controller.getCongregationsTabPageController().setMainApp(this);
			controller.getPublishersTabPageController().setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Try to load last opened person file.
//		File file = getPersonFilePath();
//		if (file != null) {
//			loadPersonDataFromFile(file);
//		}


	}

	/**
	 * @return the currentCongregation
	 */
	public CongregationModel getCurrentCongregation() {
		return this.currentCongregation;
	}

	/**
	 * @param currentCongregation the currentCongregation to set
	 */
	public  void setCurrentCongregation(CongregationModel congregation) {
		this.currentCongregation = congregation;
	}

	public static ResourceBundle getMainBundle() throws IOException {

		FileInputStream fis = new FileInputStream(BUNDLES_DIRECTORY + "main_fr.properties");


		ResourceBundle bundle = new PropertyResourceBundle(fis);

//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		InputStream inputStream = classLoader.getResourceAsStream(BUNDLES_DIRECTORY + "main_fr.properties");
//		ResourceBundle bundle  = new PropertyResourceBundle(inputStream);
		return bundle;
	}
}
