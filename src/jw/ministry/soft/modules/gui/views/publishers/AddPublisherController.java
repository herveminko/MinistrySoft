package jw.ministry.soft.modules.gui.views.publishers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.data.dao.ContactHome;
import jw.ministry.soft.modules.data.dao.PrivilegeHome;
import jw.ministry.soft.modules.data.dao.PublisherHome;
import jw.ministry.soft.modules.data.dao.PublisherPrivilegeHome;
import jw.ministry.soft.modules.data.dao.SexeHome;
import jw.ministry.soft.modules.data.dao.StatusHome;
import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.data.dto.Contact;
import jw.ministry.soft.modules.data.dto.Privilege;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.PublisherPrivilege;
import jw.ministry.soft.modules.data.dto.PublisherPrivilegeId;
import jw.ministry.soft.modules.data.dto.Sexe;
import jw.ministry.soft.modules.data.dto.Status;
import jw.ministry.soft.modules.utils.DateUtils;
import jw.ministry.soft.modules.utils.GraphicsUtils;
import jw.ministry.soft.modules.utils.HibernateUtil;

//import org.controlsfx.dialog.Dialogs;
import org.hibernate.Session;

public class AddPublisherController {

	@FXML
	private TextField firstNameTextField;
	@FXML
	private TextField lastNameTextField;
	@FXML
	private TextField phoneTextField;
	@FXML
	private TextField mobileTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField skypeTextField;
	@FXML
	private TextField streetTextField;
	@FXML
	private TextField housenumberTextField;
	@FXML
	private TextField appartmentTextField;
	@FXML
	private TextField postalCodeTextField;
	@FXML
	private TextField cityTextField;
	@FXML
	private ComboBox<String> sexComboBox;
	@FXML
	private ComboBox<String> countryComboBox;
	@FXML
	private ComboBox<String> statusComboBox;
	@FXML
    ListView<String> privilegesListView;
	@FXML
	DatePicker datePicker;


	// Reference to the main application
	private Main mainApp;
	private Stage fxStage;

	private Congregation currentCongregation;

	PublishersManagementController parentController;

	@FXML
	public void initialize() {
		ObservableList<String> countryOptions = FXCollections.observableArrayList(
				"Allemagne", "France", "Cameroun");
		ObservableList<String> sexOptions = FXCollections.observableArrayList(
				"Masculin", "Féminin");
		ObservableList<String> statusOptions = FXCollections.observableArrayList(
				"Actif", "Inactif", "Archivé", "Excommunié" );

		ObservableList<String> privilegesOptions = FXCollections
				.observableArrayList("Coordinateur", "Ancien",
						"Assistant ministériel",
						"Pionnier auxiliaire permanent", "Pionnier permanent",
						"Pionnier auxiliaire", "Surveillant de groupe",
						"Surveillant à la tour de garde", "Sécrétaire",
						"Surveillant à l'école", "Surveillant au service");



		sexComboBox.setItems(sexOptions);
		countryComboBox.setItems(countryOptions);
		statusComboBox.setItems(statusOptions);
		privilegesListView.setItems(privilegesOptions);
		privilegesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		datePicker.setConverter(new StringConverter<LocalDate>()
				{
				    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());

				    @Override
				    public String toString(LocalDate localDate)
				    {
				        if(localDate==null)
				            return "";
				        return dateTimeFormatter.format(localDate);
				    }

				    @Override
				    public LocalDate fromString(String dateString)
				    {
				        if(dateString==null || dateString.trim().isEmpty())
				        {
				            return null;
				        }
				        return LocalDate.parse(dateString,dateTimeFormatter);
				    }
				});
	}

	@FXML
	public void insertPublisher() {

		PublisherHome dao = new PublisherHome();
		ContactHome dao2 = new ContactHome();
		SexeHome dao3 = new SexeHome();
		StatusHome statusDao = new StatusHome();
		PrivilegeHome privilegesDao = new PrivilegeHome();
		PublisherPrivilegeHome pubPrivilegesDao = new PublisherPrivilegeHome();
		Publisher pub = new Publisher(firstNameTextField.getText(),
				lastNameTextField.getText());
		if (datePicker.getValue() != null) {
			pub.setBirthday(DateUtils.asDate(datePicker.getValue()));
		}


		Contact publisherContact = new Contact();
		publisherContact.setCity(cityTextField.getText());
		publisherContact.setPostalCode(postalCodeTextField.getText());
		publisherContact.setHouse(housenumberTextField.getText());
		publisherContact.setStreet(streetTextField.getText());
		publisherContact.setCountry(countryComboBox.getValue());
		publisherContact.setAppartment(appartmentTextField.getText());
		publisherContact.setCellPhone(mobileTextField.getText());
		publisherContact.setEmail(emailTextField.getText());
		publisherContact.setPhone(phoneTextField.getText());
		publisherContact.setSkypeId(skypeTextField.getText());


		Session session = HibernateUtil.getSessionFactory().openSession();

		// Handling sex
		Sexe sex = new Sexe();
		sex.setSexe(sexComboBox.getValue());
		List<Sexe> sexList = dao3.findByExample(session, sex);
		if(sexList.isEmpty()) {
			dao3.persist(session, sex);
			pub.setSexe(sex);
		} else {
			pub.setSexe(sexList.get(0));
		}

		// Handling status
		Status status = new Status();
		status.setStatus(statusComboBox.getValue());

		List<Status> statusList = statusDao.findByExample(session, status);
		if(statusList.isEmpty()) {
			statusDao.persist(session, status);
			pub.setStatus(status);
		} else {
			pub.setStatus(statusList.get(0));
		}



		dao2.persist(session, publisherContact);

		pub.setContact(publisherContact);
		pub.setCongregation(currentCongregation);
		dao.persist(session, pub);

		// Handling privileges
		ObservableList<String> privilegesList = privilegesListView.getSelectionModel().getSelectedItems();
		for (String priv : privilegesList) {
			Privilege privilege = new Privilege(priv);
			List<Privilege> privList = privilegesDao.findByExample(session, privilege);
			if(privList.isEmpty()) {
				privilegesDao.persist(session, privilege);
			} else {
				privilege = privList.get(0);
			}

			PublisherPrivilege pubPrivilege = new PublisherPrivilege();
			pubPrivilege.setPrivilege(privilege);
			pubPrivilege.setPublisher(pub);
			PublisherPrivilegeId id = new PublisherPrivilegeId(pub.getPublisherId(),privilege.getPrivilegeId());
			pubPrivilege.setId(id);

			pubPrivilegesDao.persist(session, pubPrivilege);
			pub.getPublisherPrivileges().add(pubPrivilege);
		}

		dao.persist(session, pub);

		session.close();



		GraphicsUtils.openInformationDialog("Insertion de Proclamateur", "Proclamateur correctement inséré!",
				null);

		getParentController().getAllPublisherInCongregation();

		close();

	}

	/**
	 * @return the parentController
	 */
	public PublishersManagementController getParentController() {
		return parentController;
	}

	/**
	 * @param parentController
	 *            the parentController to set
	 */
	public void setParentController(
			PublishersManagementController parentController) {
		this.parentController = parentController;
	}

	@FXML
	public void close() {
		getFxStage().close();
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
	 * @return the currentCongregation
	 */
	public Congregation getCurrentCongregation() {
		return currentCongregation;
	}

	/**
	 * @param currentCongregation
	 *            the currentCongregation to set
	 */
	public void setCurrentCongregation(Congregation currentCongregation) {
		this.currentCongregation = currentCongregation;
	}

}
