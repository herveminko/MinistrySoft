package fxmlviews;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.data.exchange.exports.PublishersExporter;
import jw.ministry.soft.modules.data.exchange.exports.TerritoriesAssignmentsExporter;
import jw.ministry.soft.modules.data.exchange.exports.TerritoriesExporter;
import jw.ministry.soft.modules.data.exchange.imports.TerritoryBoundsMapImporter;
import jw.ministry.soft.modules.gui.views.congregation.CongregationsListController;
import jw.ministry.soft.modules.gui.views.publishers.PublishersManagementController;
import jw.ministry.soft.modules.gui.views.territories.TerritoriesController;
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
public class RootController {

	// Reference to the main application
	private Main mainApp;

	AnchorPane layout;

	Stage fxStage;

	@FXML
	private CongregationsListController congregationsTabPageController;

	@FXML
	private PublishersManagementController publishersTabPageController;

	@FXML
	private TerritoriesController territoriesTabPageController;

	@FXML
	public void initialize() {
		getCongregationsTabPageController().setMainApp(this.mainApp);
		getCongregationsTabPageController().setRootParentController(this);

		getPublishersTabPageController().setMainApp(this.mainApp);
		getPublishersTabPageController().setRootParentController(this);

		getTerritoriesTabPageController().setRootParentController(this);
		getTerritoriesTabPageController().initialize();

	}

	@FXML
	public void exportAllCongregationPublishers() {
		int congregationId = 1;

		if (mainApp != null && mainApp.getCurrentCongregation() != null) {
			congregationId = mainApp.getCurrentCongregation()
					.getCongregationId().get();
		}
		PublishersExporter.exportPublishersFromCongregaton(congregationId - 1);

	}

	@FXML
	public void importTerritoryAssistantMaps()  {
		try {
			TerritoryBoundsMapImporter.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void exportAllTerritories() {
		TerritoriesExporter.exportTerritories();
	}

	@FXML
	public void exportAllTerritoriesAssignments() {
		TerritoriesAssignmentsExporter.exportTerritoriAssignments();
		TerritoriesAssignmentsExporter.exportPubishersTerritoriesAssignments();
	}

	public void updtateCongregationsList() {
		getPublishersTabPageController().getAllPublisherInCongregation();
	}

	public void updatePublishersList() {
		getTerritoriesTabPageController().updatePublishersList();
	}

	/**
	 */
	public Stage getFxStage() {
		return fxStage;
	}

	/**
	 * @param fxStage
	 *            the fxStage to set
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
	 * @throws IOException
	 */
	@FXML
	private void handleAbout() throws IOException {

    	String appVersion = "";
    	ResourceBundle bundle;
		try {
			bundle = Main.getMainBundle();
        	appVersion = bundle.getString("app_version");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GraphicsUtils.openInformationDialog("MinistrySoftApp " + appVersion, "Programme utilitaire pour la gestion de territoires.\n\nAuteurs: Hervé Minko; Hervé Ngassop :-)\nContact: hcminko@hotmail.com", "À propos...");
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
	private void openCongregationManagementDialog() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootController.class
					.getResource("congregation/CongregationsList.fxml"));
			layout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(layout);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Congregation list");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(fxStage);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/calendar.png"));
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			CongregationsListController controller = loader.getController();
			controller.setMainApp(mainApp);

			dialogStage.show();

			// dialogStage.addEventHandler(eventType, eventHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Open the publishers management dialog.
	 */
	@FXML
	private void openPublishersManagementDialog() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootController.class
					.getResource("publishers/PublishersManagement.fxml"));
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

			// dialogStage.addEventHandler(eventType, eventHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * @return the congregationsTabPageController
	 */
	public CongregationsListController getCongregationsTabPageController() {
		return congregationsTabPageController;
	}

	/**
	 * @param congregationsTabPageController
	 *            the congregationsTabPageController to set
	 */
	public void setCongregationsTabPageController(
			CongregationsListController congregationsTabPageController) {
		this.congregationsTabPageController = congregationsTabPageController;
	}

	/**
	 * @return the publishersTabPageController
	 */
	public PublishersManagementController getPublishersTabPageController() {
		return publishersTabPageController;
	}

	/**
	 * @param publishersTabPageController
	 *            the publishersTabPageController to set
	 */
	public void setPublishersTabPageController(
			PublishersManagementController publishersTabPageController) {
		this.publishersTabPageController = publishersTabPageController;
	}

	/**
	 * @return the territoriesTabPageController
	 */
	public TerritoriesController getTerritoriesTabPageController() {
		return territoriesTabPageController;
	}

	/**
	 * @param territoriesTabPageController
	 *            the territoriesTabPageController to set
	 */
	public void setTerritoriesTabPageController(
			TerritoriesController territoriesTabPageController) {
		this.territoriesTabPageController = territoriesTabPageController;
	}

}