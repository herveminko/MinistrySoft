/**
 *
 */
package jw.ministry.soft.modules.gui.views.publishers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//import org.controlsfx.dialog.Dialog;
//import org.controlsfx.dialog.Dialogs;
import org.hibernate.Session;

import fxmlviews.RootController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.data.dao.CongregationHome;
import jw.ministry.soft.modules.data.dao.PublisherHome;
import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.gui.views.publishers.model.PublisherModel;
import jw.ministry.soft.modules.utils.GraphicsUtils;
import jw.ministry.soft.modules.utils.HibernateUtil;

/**
 * @author HervéClaude
 *
 */
public class PublishersManagementController {

	Congregation selectedCongregation;
	Publisher currentPublisher;
	RootController rootParentController;

	PublisherModel selectedPublisherModel;

	AnchorPane layout;

	@FXML
	private TableView<PublisherModel> publishersTable;
	@FXML
	private TableColumn<PublisherModel, String> publisherFirstNameColumn;
	@FXML
	private TableColumn<PublisherModel, String> publisherLastNameColumn;
	@FXML
	private TableColumn<PublisherModel, String> sexColumn;

	@FXML
	private ComboBox<String> congregationListComboBox;
	@FXML
	private TitledPane changeCongregationTitledPane;

	List<Congregation> dbCongregations;

	ObservableList<String> congregationsOptions = FXCollections.observableArrayList();

	// Reference to the main application
	private Main mainApp;
	private Stage fxStage;

	/**
	 * The data as an observable list of publishers.
	 */
	private ObservableList<PublisherModel> publishersData = FXCollections.observableArrayList();

	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label sexeLabel;
	@FXML
	private Label phoneLabel;
	@FXML
	private Label mobileLabel;
	@FXML
	private Label emailLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label housenumberLabel;
	@FXML
	private Label appartmentLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label countryLabel;
	@FXML
	private TitledPane publisherTitledPane;
	@FXML
	private Label dateOfBirthLabel;
	@FXML
	private Label skypeIdLabel;
	@FXML
	private Label privilegesLabel;
	@FXML
	private Label territoriesLabel;
	@FXML
	private Label statusLabel;

	@FXML
	private Accordion accordionPane;

	@FXML
	private GridPane publisherDetails;

	@FXML
	public void initialize() {

		getAllPublisherInCongregation();

		// this.selectedCongregation =
		// mainApp.getCurrentCongregation().getCongregation();
		// getAllCongregationsFromDatabase();
		//
		// Initialize the person table with the two columns.
		publisherFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getPublisherFirstNameProperty());
		publisherLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getPublisherLastNameProperty());
		sexColumn.setCellValueFactory(cellData -> cellData.getValue().getPublisherSexeProperty());
		//
		// Listen for selection changes and show the person details when
		// changed.

		// Set data in ascending order
		publisherLastNameColumn.setSortType(TableColumn.SortType.ASCENDING);

		publishersTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> updatePublisherDetails(newValue));

		publishersTable.getSortOrder().add(publisherLastNameColumn);

		congregationListComboBox.getSelectionModel().selectedIndexProperty()
				.addListener((observable, oldValue, newValue) -> getAllPublisherInCongregation());

		// Expand the publisher's details pane
		// accordionPane.setExpandedPane(publisherTitledPane);

	}

	/**
	 * Retrieve all congregation publishers from the database and build the UI
	 * model.
	 */
	@FXML
	public void getAllPublisherInCongregation() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		CongregationHome dao = new CongregationHome();
		dbCongregations = dao.getAllCongregations(session);
		if (!dbCongregations.isEmpty()) {
			congregationsOptions = FXCollections.observableArrayList();
			for (Congregation c : dbCongregations) {
				congregationsOptions.add(c.getCongregationName());
			}

			this.congregationListComboBox.setItems(congregationsOptions);
			int selectedIndex = congregationListComboBox.getSelectionModel().getSelectedIndex();
			if (selectedIndex < 0) {
				selectedIndex = 0;
			}

			this.congregationListComboBox.setValue(congregationsOptions.get(selectedIndex));
			selectedCongregation = dbCongregations.get(selectedIndex);

			// PublisherHome dao = new PublisherHome();
			if (selectedCongregation != null) {
				loadCongregationPublishers(session, selectedCongregation);
			}
		}

		session.close();

		if (getRootParentController() != null) {
			getRootParentController().updatePublishersList();
		}

	}

	public void loadCongregationPublishers(Session session, Congregation congre) {
		// this.changeCongregationTitledPane.setText("Congregation sélectionnée:
		// "
		// + selectedCongregation.getCongregationName());
		Set<Publisher> dbPublishers = selectedCongregation.getPublishers(); // dao.getAllPublishersInCongregation(selectedCongregation.session);

		List<PublisherModel> publishersModelList = new ArrayList<PublisherModel>();
		for (Publisher p : dbPublishers) {
			publishersModelList.add(new PublisherModel(p));
		}

		this.publishersData.clear();
		this.publishersData.addAll(publishersModelList);
		// Add observable list data to the table
		publishersTable.setItems(publishersData);

		publishersTable.getSortOrder().add(publisherLastNameColumn);
		publishersTable.getSortOrder().add(publisherFirstNameColumn);
		publisherLastNameColumn.setSortable(true);
		publisherFirstNameColumn.setSortable(true);
	}

	/**
	 * Display the list of all publisher of a congregation
	 */
	@FXML
	public void showPublishersList() {
		// selectedCongregation
	}

	/**
	 * Display the list of territories of a given publisher
	 */
	@FXML
	public void showPublishersTerritoriesList() {

		if (currentPublisher != null) {

			Set<Territoriesassignments> assignments = currentPublisher.getTerritoriesassignmentses();
			String listString = "";

			for (Territoriesassignments assignment : assignments) {
				if (assignment.getReturnDate() == null) {
					Territory ter = assignment.getTerritory();
					listString += ter.getCode() + " # " + ter.getName() + " (" + ter.getTerritoryType() + ")\n";
				}
			}

			if (!listString.isEmpty()) {

				GraphicsUtils.openInformationDialog("Territoires Proclamateur", "Territoires assignés au prolameteur\n" + listString,
						null);
			}
		}
	}

	/**
	 * Show the properties the given publisher.
	 */
	@FXML
	public void showPublisherDetails() {
		PublisherModel publisherModel = publishersTable.getSelectionModel().getSelectedItem();
		if (publisherModel != null) {
			// resetPublisherDetails();
			updatePublisherDetails(publisherModel);

			// accordionPane.setExpandedPane(changeCongregationTitledPane);
			// try {
			// Thread.sleep(2000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// accordionPane.setExpandedPane(publisherTitledPane);
		}

	}

	public void resetPublisherDetails() {
		firstNameLabel.setText("");
		lastNameLabel.setText("");
		sexeLabel.setText("");
		phoneLabel.setText("");
		mobileLabel.setText("");
		streetLabel.setText("");
		housenumberLabel.setText("");
		appartmentLabel.setText("");
		postalCodeLabel.setText("");
		cityLabel.setText("");
		countryLabel.setText("");
		emailLabel.setText("");
		dateOfBirthLabel.setText("");
		skypeIdLabel.setText("");
		statusLabel.setText("");
		privilegesLabel.setText("");
		territoriesLabel.setText("");
	}

	public void requestPublisherLabelsLayout() {
		firstNameLabel.requestLayout();
		lastNameLabel.requestLayout();
		sexeLabel.requestLayout();
		phoneLabel.requestLayout();
		mobileLabel.requestLayout();
		streetLabel.requestLayout();
		housenumberLabel.requestLayout();
		appartmentLabel.requestLayout();
		postalCodeLabel.requestLayout();
		cityLabel.requestLayout();
		countryLabel.requestLayout();
		emailLabel.requestLayout();
		dateOfBirthLabel.requestLayout();
		skypeIdLabel.requestLayout();
		statusLabel.requestLayout();
		privilegesLabel.requestLayout();
		territoriesLabel.requestLayout();
	}

	public void updatePublisherDetails(PublisherModel publisherModel) {
		currentPublisher = publisherModel.getPublisherRecord();
		firstNameLabel.setText(publisherModel.getPublisherFirstNameProperty().getValue());
		lastNameLabel.setText(publisherModel.getPublisherLastNameProperty().getValue());
		sexeLabel.setText(publisherModel.getPublisherSexeProperty().getValue());
		phoneLabel.setText(publisherModel.getPublisherPhoneNumberProperty().getValue());
		mobileLabel.setText(publisherModel.getPublisherMobileNumberProperty().getValue());
		streetLabel.setText(publisherModel.getPublisherStreetProperty().getValue());
		housenumberLabel.setText(publisherModel.getPublisherHouseNumberProperty().getValue());
		appartmentLabel.setText(publisherModel.getPublisherAppartmentProperty().getValue());
		postalCodeLabel.setText(publisherModel.getPublisherPostalCoderProperty().getValue());
		cityLabel.setText(publisherModel.getPublisherCityProperty().getValue());
		countryLabel.setText(publisherModel.getPublisherCountryProperty().getValue());
		emailLabel.setText(publisherModel.getPublisherEmailProperty().getValue());
		if (publisherModel.getPublisherDateOfBirthProperty() != null) {
			dateOfBirthLabel.setText(publisherModel.getPublisherDateOfBirthProperty().getValue());
		} else {
			dateOfBirthLabel.setText(null);
		}
		skypeIdLabel.setText(publisherModel.getPublisherSkypeIdProperty().getValue());
		statusLabel.setText(publisherModel.getPublisherStatusProperty().getValue());
		String text = "";
		for (String pr : publisherModel.getPublisherPrivileges()) {
			text += pr + " ; ";
		}
		privilegesLabel.setText(text);

		text = "";
		for (String pr : publisherModel.getPublisherTerritories()) {
			text += pr + " ; ";
		}
		territoriesLabel.setText(text);

		// accordionPane.setExpandedPane(changeCongregationTitledPane);
		// // Expand the publisher's details pane
		// accordionPane.setExpandedPane(publisherTitledPane);

	}

	/**
	 * Delete selected publisher record to the database.
	 */
	@FXML
	private void deletePublisher() {

		Alert alert = GraphicsUtils.openConfirmationDialog("Effacer Prolameteur", "Voulez-vous vraiment effacer le proclamateur de la base de données?", null);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.YES) {
			// Effacer le proclamateur
			PublisherHome dao = new PublisherHome();
			dao.delete(currentPublisher);
			getAllPublisherInCongregation();
		} else {
			// ... user chose NO, CANCEL, or closed the dialog
		}

	}

	/**
	 * ADD a publisher to a congregation the list of all publisher of a
	 * congregation
	 */
	@FXML
	public void addNewPublisher() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AddPublisherController.class.getResource("AddPublisherDialog.fxml"));
			layout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(layout);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Publisher information");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(fxStage);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/calendar.png"));
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			AddPublisherController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setFxStage(dialogStage);
			controller.initialize();

			controller.setCurrentCongregation(selectedCongregation);

			controller.setParentController(this);

			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ADD a publisher to a congregation the list of all publisher of a
	 * congregation
	 */
	@FXML
	public void showEditPublisherDialog() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AddPublisherController.class.getResource("EditPublisherDialog.fxml"));
			layout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(layout);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Publisher information");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(fxStage);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/calendar.png"));
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			EditPublisherController controller = loader.getController();
			controller.setPublisher(currentPublisher);
			controller.setMainApp(mainApp);
			controller.setFxStage(dialogStage);

			controller.setCurrentCongregation(selectedCongregation);

			controller.setParentController(this);

			dialogStage.show();

			controller.initialize();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the selectedCongregation
	 */
	public Congregation getSelectedCongregation() {
		return selectedCongregation;
	}

	/**
	 * @param selectedCongregation
	 *            the selectedCongregation to set
	 */
	public void setSelectedCongregation(Congregation selectedCongregation) {
		this.selectedCongregation = selectedCongregation;
	}

	/**
	 * @return the currentPublisher
	 */
	public Publisher getCurrentPublisher() {
		return currentPublisher;
	}

	/**
	 * @param currentPublisher
	 *            the currentPublisher to set
	 */
	public void setCurrentPublisher(Publisher currentPublisher) {
		this.currentPublisher = currentPublisher;
	}

	/**
	 * @return the mainApp
	 */
	public Main getMainApp() {
		return mainApp;
	}

	/**
	 * @param mainApp
	 *            the mainApp to set
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
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
	 * @return the selectedPublisherModel
	 */
	public PublisherModel getSelectedPublisherModel() {
		return selectedPublisherModel;
	}

	/**
	 * @param selectedPublisherModel
	 *            the selectedPublisherModel to set
	 */
	public void setSelectedPublisherModel(PublisherModel selectedPublisherModel) {
		this.selectedPublisherModel = selectedPublisherModel;
	}

	/**
	 * @return the rootParentController
	 */
	public RootController getRootParentController() {
		return rootParentController;
	}

	/**
	 * @param rootParentController
	 *            the rootParentController to set
	 */
	public void setRootParentController(RootController rootParentController) {
		this.rootParentController = rootParentController;
	}

}
