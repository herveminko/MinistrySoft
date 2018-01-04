package jw.ministry.soft.modules.gui.views.congregation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.data.dao.CongregationHome;
import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.gui.views.congregation.model.CongregationModel;
import jw.ministry.soft.modules.utils.HibernateUtil;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.hibernate.Session;

import fxmlviews.RootController;

/**
 * Controller class for the ui view CongregationsList.fxml.
 *
 * @author Hervé Minko
 *
 */
public class CongregationsListController {

	@FXML
	private TableView<CongregationModel> congregationTable;
	@FXML
	private TableColumn<CongregationModel, String> congregationNameColumn;
	@FXML
	private TableColumn<CongregationModel, String> congregationCountryColumn;
//	@FXML
//	private TableHeaderRow


    @FXML
    private Label congregationNameLabel;
    @FXML
    private Label congregationCountryLabel;
    @FXML
    private Label congregationLanguageLabel;

	// Reference to the main application
	private Main mainApp;

	private Stage fxStage;

	CongregationModel currentModel;

	AnchorPane layout;

	/**
	 * The data as an observable list of congregations.
	 */
	private ObservableList<CongregationModel> congregationsData = FXCollections
			.observableArrayList();

	RootController rootParentController;
	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param mainApp
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * @return the currentModel
	 */
	public CongregationModel getCurrentModel() {
		return currentModel;
	}

	/**
	 * @param currentModel
	 *            the currentModel to set
	 */
	public void setCurrentModel(CongregationModel currentModel) {
		this.currentModel = currentModel;
	}

	public void showCongregationDetails(CongregationModel congregationModel) {
		if (congregationModel != null) {
			currentModel = congregationModel;
			mainApp.setCurrentCongregation(currentModel);
			congregationNameLabel.setText(congregationModel.getCongregationName().getValue());
			congregationCountryLabel.setText(congregationModel.getCountry().getValue());
			congregationLanguageLabel.setText(congregationModel.getCongregationLanguage().getValue());
		}
	}

	@FXML
	public void initialize() {
		getAllCongregationsFromDatabase();

    	// Initialize the person table with the two columns.
		congregationNameColumn.setCellValueFactory(
        		cellData -> cellData.getValue().getCongregationName());
        congregationCountryColumn.setCellValueFactory(
        		cellData -> cellData.getValue().getCountry());

        // Listen for selection changes and show the person details when changed.
		congregationTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showCongregationDetails(newValue));
	}

	/**
	 * Retrieve all congregations from the database and build the UI model.
	 */
	@FXML
	public  void getAllCongregationsFromDatabase() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		CongregationHome dao = new CongregationHome();
		List<Congregation> dbCongregations = dao.getAllCongregations(session);

		List<CongregationModel> congregationModelList = new ArrayList<CongregationModel>();
		for (Congregation c : dbCongregations) {
			congregationModelList.add(new CongregationModel(c));
		}

		session.close();
		congregationsData.clear();
		congregationsData.addAll(congregationModelList);
		// Add observable list data to the table
		congregationTable.setItems(congregationsData);
		if (getRootParentController() != null) {
		getRootParentController().updtateCongregationsList();
		}



	}

	/**
	 * Delete selected congregation record to the database.
	 */
	@FXML
	private void deleteCongregation() {
		Action response = Dialogs.create()
		        .owner(getFxStage())
		        .title("Effacer Congregation")
		        .masthead("")
		        .message("Voulez-vous vraiment effacer la congrégation de la base de données?")
		        .showConfirm();

		if (response == Dialog.Actions.YES) {
			// Effacer la congrégation
			Congregation congregation = currentModel.getCongregation();
			CongregationHome dao = new CongregationHome();
			dao.delete(congregation);
			getAllCongregationsFromDatabase();
		} else {
		    // ... user chose NO, CANCEL, or closed the dialog
		}


	}

	/**
	 * Insert a new congregation record to the database.
	 */
	@FXML
	private void addNewCongregation() {

		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CongregationAddController.class
					.getResource("AddCongregation.fxml"));
			layout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(layout);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Congregation information");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(fxStage);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/calendar.png"));
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			CongregationAddController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setFxStage(dialogStage);
			controller.initialize();
			controller.setParentController(this);

			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Insert a new congregation record to the database.
	 */
	@FXML
	private void editSelectedCongregation() {

		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CongregationAddController.class
					.getResource("EditCongregation.fxml"));
			layout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(layout);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Congregation information");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(fxStage);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/calendar.png"));
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			CongregationAddController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setFxStage(dialogStage);
			controller.setCongregationModel(currentModel);
			controller.initialize();
			controller.setParentController(this);



			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the fxStage
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
	 * @return the rootParentController
	 */
	public RootController getRootParentController() {
		return rootParentController;
	}

	/**
	 * @param rootParentController the rootParentController to set
	 */
	public void setRootParentController(RootController rootParentController) {
		this.rootParentController = rootParentController;
	}



}
