package jw.ministry.soft.modules.gui.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.gui.views.congregation.CongregationsListController;
import jw.ministry.soft.modules.gui.views.publishers.PublishersManagementController;
import jw.ministry.soft.modules.utils.GraphicsUtils;
import jw.ministry.soft.modules.utils.HibernateUtil;

//import org.controlsfx.dialog.Dialogs;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Hervé Minko
 */
public class RootLayoutController {


    // Reference to the main application
    private Main mainApp;

    AnchorPane layout;

    Stage fxStage;



    /**
	 * @return the fxStage
	 */
	public Stage getFxStage() {
		return fxStage;
	}



	/**
	 * @param fxStage the fxStage to set
	 */
	public void setFxStage(Stage fxStage) {
		this.fxStage = fxStage;
	}



	/**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }



    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {

		GraphicsUtils.openInformationDialog("MinistrySoftApp",
				"Auteurs: Hervé Minko; Hervé Ngassop\nWebsite: http://www.ministrysoft.org", "À propos");

    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
    	HibernateUtil.shutdown();
        System.exit(0);
    }

    /**
     * Open the congregation management dialog.
     */
    @FXML
    private void openCongregationManagementDialog(){
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("congregation/CongregationsList.fxml"));
			layout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(layout);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Congregation list");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(fxStage);
			//dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));
			dialogStage.setScene(scene);



			// Give the controller access to the main app.
			CongregationsListController controller = loader.getController();
			controller.setMainApp(mainApp);

			dialogStage.show();

//			dialogStage.addEventHandler(eventType, eventHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

    /**
     * Open the publishers management dialog.
     */
    @FXML
    private void openPublishersManagementDialog(){
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("publishers/PublishersManagement.fxml"));
			layout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(layout);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Publishers Management");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(fxStage);
			dialogStage.setScene(scene);


			// Give the controller access to the main app.
			PublishersManagementController controller = loader.getController();
			controller.setMainApp(mainApp);

			dialogStage.show();

//			dialogStage.addEventHandler(eventType, eventHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

    }


}