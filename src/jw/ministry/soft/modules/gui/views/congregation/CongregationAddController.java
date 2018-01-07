package jw.ministry.soft.modules.gui.views.congregation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.data.dao.CongregationHome;
import jw.ministry.soft.modules.data.dao.ContactHome;
import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.data.dto.Contact;
import jw.ministry.soft.modules.gui.views.congregation.model.CongregationModel;
import jw.ministry.soft.modules.utils.GraphicsUtils;
import jw.ministry.soft.modules.utils.HibernateUtil;

//import org.controlsfx.dialog.Dialogs;
import org.hibernate.Session;

/**
 * Controller class for the ui view CongregationsList.fxml.
 *
 * @author Hervé Minko
 *
 */
public class CongregationAddController {

	ObservableList<String> languageOptions = FXCollections.observableArrayList("Francais", "Anglais", "Allemand");
	ObservableList<String> countryOptions = FXCollections.observableArrayList("Allemagne", "France", "Cameroun");

	@FXML
	private TextField congregationNameField;

	@FXML
	private ComboBox<String> congregationLanguagesComboBox;

	@FXML
	private ComboBox<String> congregationCountriesComboBox;

	// Reference to the main application
	private Main mainApp;

	private Stage fxStage;

	CongregationModel congregationModel;

	private CongregationsListController parentController;

	public CongregationAddController() {

	}

	/**
	 * Initialize some slots.
	 */
	public void initialize() {

		this.congregationCountriesComboBox.setItems(countryOptions);
		this.congregationCountriesComboBox.setValue(countryOptions.get(0));
		this.congregationLanguagesComboBox.setItems(languageOptions);
		this.congregationLanguagesComboBox.setValue(languageOptions.get(0));

		if (this.congregationModel != null) {
			congregationNameField.setText(congregationModel.getCongregation().getCongregationName());
			congregationCountriesComboBox.setValue(congregationModel.getCongregation().getContact().getCountry());
			congregationLanguagesComboBox.setValue(congregationModel.getCongregation().getCongregationLanguage());
		}
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
	 * Modify a congregation record in the database.
	 */
	@FXML
	public void editCongregation() {
		// Dialogs.create().owner(getFxStage()).title("Edit Congregation
		// Dialog")
		// .masthead(null).message("I have a great message for you!")
		// .showInformation();
		CongregationHome dao = new CongregationHome();
		ContactHome dao2 = new ContactHome();
		Session session = HibernateUtil.getSessionFactory().openSession();

		Congregation congre = dao.findById(session, this.congregationModel.getCongregation().getCongregationId());
		congre.setCongregationLanguage(congregationLanguagesComboBox.getValue());
		congre.setCongregationName(congregationNameField.getText());
		Contact contact = congre.getContact();
		contact.setCountry(congregationCountriesComboBox.getValue());
		dao2.persist(session, contact);

		congre.setContact(contact);

		dao.persist(session, congre);

		session.close();


		GraphicsUtils.openInformationDialog("Modification de congrégation", "Congregation correctement modifiée!",
				null);

		close();

		getParentController().getAllCongregationsFromDatabase();
	}

	/**
	 * Insert a new congregation record to the database.
	 */
	@FXML
	public void addCongregation() {

		CongregationHome dao = new CongregationHome();
		ContactHome dao2 = new ContactHome();
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Congregation congre = new
		// Congregation(congregationNameField.getText(),
		// congregationLanguagesComboBox.getValue(),
		// congregationCountriesComboBox.getValue());
		Congregation congre = new Congregation(congregationNameField.getText(),
				congregationLanguagesComboBox.getValue());
		Contact contact = new Contact();
		contact.setCountry(congregationCountriesComboBox.getValue());
		dao2.persist(session, contact);

		congre.setContact(contact);

		dao.persist(session, congre);

		session.close();

		GraphicsUtils.openInformationDialog("Insertion de congrégation", "Congrégation correctement insérée!!",
				null);

		close();

		getParentController().getAllCongregationsFromDatabase();

	}

	@FXML
	public void close() {
		getFxStage().close();
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
	 * @return the parentController
	 */
	public CongregationsListController getParentController() {
		return parentController;
	}

	/**
	 * @param parentController
	 *            the parentController to set
	 */
	public void setParentController(CongregationsListController parentController) {
		this.parentController = parentController;
	}

	/**
	 * @return the congregationModel
	 */
	public CongregationModel getCongregationModel() {
		return congregationModel;
	}

	/**
	 * @param congregationModel
	 *            the congregationModel to set
	 */
	public void setCongregationModel(CongregationModel congregationModel) {
		this.congregationModel = congregationModel;
	}

}
