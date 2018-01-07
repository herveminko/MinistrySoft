package jw.ministry.soft.modules.gui.views.publishers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import jw.ministry.soft.modules.data.dao.PublishinggroupHome;
import jw.ministry.soft.modules.data.dao.SexeHome;
import jw.ministry.soft.modules.data.dao.StatusHome;
import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.data.dto.Contact;
import jw.ministry.soft.modules.data.dto.Privilege;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.PublisherPrivilege;
import jw.ministry.soft.modules.data.dto.PublisherPrivilegeId;
import jw.ministry.soft.modules.data.dto.Publishinggroup;
import jw.ministry.soft.modules.data.dto.Sexe;
import jw.ministry.soft.modules.data.dto.Status;
import jw.ministry.soft.modules.utils.DateUtils;
import jw.ministry.soft.modules.utils.GraphicsUtils;
import jw.ministry.soft.modules.utils.HibernateUtil;

//import org.controlsfx.dialog.Dialogs;
import org.hibernate.Session;

public class EditPublisherController {

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
	private ComboBox<String> groupComboBox;
	@FXML
	ListView<String> privilegesListView;

	@FXML
	DatePicker datePicker;

	// Reference to the main application
	private Main mainApp;
	private Stage fxStage;

	private Publisher publisher;

	private Congregation currentCongregation;

	PublishersManagementController parentController;

	@FXML
	public void initialize() {
		ObservableList<String> countryOptions = FXCollections
				.observableArrayList("Allemagne", "France", "Cameroun");
		ObservableList<String> sexOptions = FXCollections.observableArrayList(
				"Masculin", "Féminin");
		ObservableList<String> statusOptions = FXCollections
				.observableArrayList("Actif", "Inactif", "Archivé",
						"Excommunié");

		ObservableList<String> privilegesOptions = FXCollections
				.observableArrayList("Coordinateur", "Ancien",
						"Assistant ministériel",
						"Pionnier auxiliaire permanent", "Pionnier permanent",
						"Pionnier auxiliaire", "Surveillant de groupe",
						"Surveillant à la tour de garde", "Sécrétaire",
						"Surveillant à l'école", "Surveillant au service");

		ObservableList<String> groupOptions = FXCollections
				.observableArrayList("Bielefeld", "Paderborn", "Osnabrück");

		sexComboBox.setItems(sexOptions);
		countryComboBox.setItems(countryOptions);
		statusComboBox.setItems(statusOptions);
		groupComboBox.setItems(groupOptions);
		privilegesListView.setItems(privilegesOptions);
		privilegesListView.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);

		if (publisher != null) {
			firstNameTextField.setText(publisher.getFirstName());
			lastNameTextField.setText(publisher.getLastName());
			if (publisher.getStatus() != null) {
				statusComboBox.setValue(publisher.getStatus().getStatus());
			}
			if (publisher.getContact() != null) {

				skypeTextField.setText(publisher.getContact().getSkypeId());
				streetTextField.setText(publisher.getContact().getStreet());
				housenumberTextField.setText(publisher.getContact().getHouse());
				postalCodeTextField.setText(publisher.getContact()
						.getPostalCode());
				cityTextField.setText(publisher.getContact().getCity());
				countryComboBox.setValue(publisher.getContact().getCountry());
				appartmentTextField.setText(publisher.getContact()
						.getAppartment());

				phoneTextField.setText(publisher.getContact().getPhone());
				mobileTextField.setText(publisher.getContact().getCellPhone());
				emailTextField.setText(publisher.getContact().getEmail());
				if (publisher.getBirthday() != null) {
					datePicker.setValue(DateUtils.asLocalDate(publisher
							.getBirthday()));
				}
			}

			if (publisher.getSexe() != null) {
				sexComboBox.setValue(publisher.getSexe().getSexe());
			}

			if (publisher.getPublishinggroup() != null) {
				groupComboBox
						.setValue(publisher.getPublishinggroup().getName());
			}

			if (publisher.getPublisherPrivileges() != null) {
				Set<PublisherPrivilege> listOfPrivs = publisher
						.getPublisherPrivileges();
				List<String> privilegesList = new ArrayList<String>();
				for (PublisherPrivilege p : listOfPrivs) {
					privilegesList.add(p.getPrivilege().getPrivilege());
					privilegesListView.getSelectionModel().select(
							p.getPrivilege().getPrivilege());
					;
				}

			}
		}

		datePicker.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter
					.ofPattern(DateUtils.getUiDateFormat());

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});

	}

	@FXML
	public void modifyPublisher() {

		PublisherHome dao = new PublisherHome();
		ContactHome dao2 = new ContactHome();
		SexeHome dao3 = new SexeHome();
		StatusHome statusDao = new StatusHome();
		PrivilegeHome privilegesDao = new PrivilegeHome();
		PublisherPrivilegeHome pubPrivilegesDao = new PublisherPrivilegeHome();
		PublishinggroupHome groupDao = new PublishinggroupHome();

		Publisher pub = publisher;

		Session session = HibernateUtil.getSessionFactory().openSession();
		PublisherHome pubDao = new PublisherHome();
		Publisher dbPub = pubDao.findById(session, pub.getPublisherId());

		Sexe sex = new Sexe();
		sex.setSexe(sexComboBox.getValue());
		List<Sexe> testList = dao3.findByExample(session, sex);
		if (testList.isEmpty()) {
			dao3.persist(session, sex);
			dbPub.setSexe(sex);
		} else {
			dbPub.setSexe(testList.get(0));
		}

		Status status = new Status();
		status.setStatus(statusComboBox.getValue());

		List<Status> statusList = statusDao.findByExample(session, status);
		if (statusList.isEmpty()) {
			statusDao.persist(session, status);
			dbPub.setStatus(status);
		} else {
			dbPub.setStatus(statusList.get(0));
		}

		Publishinggroup group = new Publishinggroup();
		group.setName(groupComboBox.getValue());
		List<Publishinggroup> groupsList = groupDao.findByExample(session,
				group);
		if (groupsList.isEmpty()) {
			groupDao.persist(session, group);
			dbPub.setPublishinggroup(group);
		} else {
			dbPub.setPublishinggroup(groupsList.get(0));
		}

		dbPub.setFirstName(firstNameTextField.getText());
		dbPub.setLastName(lastNameTextField.getText());
		if (datePicker.getValue() != null) {
			dbPub.setBirthday(DateUtils.asDate(datePicker.getValue()));
		}

		Contact publisherContactData = dbPub.getContact();
		publisherContactData.setCity(cityTextField.getText());
		publisherContactData.setPostalCode(postalCodeTextField.getText());
		publisherContactData.setHouse(housenumberTextField.getText());
		publisherContactData.setStreet(streetTextField.getText());
		publisherContactData.setAppartment(appartmentTextField.getText());
		publisherContactData.setCountry(countryComboBox.getValue());
		publisherContactData.setCellPhone(mobileTextField.getText());
		publisherContactData.setEmail(emailTextField.getText());
		publisherContactData.setPhone(phoneTextField.getText());
		publisherContactData.setSkypeId(skypeTextField.getText());

		dao2.persist(session, publisherContactData);

		dbPub.setContact(publisherContactData);

		// Handling privileges
		ObservableList<String> privilegesList = privilegesListView
				.getSelectionModel().getSelectedItems();

		for (PublisherPrivilege p : dbPub.getPublisherPrivileges()) {
			pubPrivilegesDao.delete(session, p);
		}

		for (String priv : privilegesList) {
			Privilege privilege = new Privilege(priv);
			List<Privilege> privList = privilegesDao.findByExample(session,
					privilege);
			if (privList.isEmpty()) {
				privilegesDao.persist(session, privilege);
			} else {
				privilege = privList.get(0);
			}

			PublisherPrivilege pubPrivilege = new PublisherPrivilege();
			pubPrivilege.setPrivilege(privilege);
			pubPrivilege.setPublisher(dbPub);
			PublisherPrivilegeId id = new PublisherPrivilegeId(
					pub.getPublisherId(), privilege.getPrivilegeId());
			pubPrivilege.setId(id);

			pubPrivilegesDao.persist(session, pubPrivilege);
			dbPub.getPublisherPrivileges().add(pubPrivilege);
		}

		dao.persist(session, dbPub);

		session.close();

		GraphicsUtils.openInformationDialog("Modification de Proclamateur", "Proclamateur correctement modifié!",
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

	/**
	 * @return the publisher
	 */
	public Publisher getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher
	 *            the publisher to set
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

}
