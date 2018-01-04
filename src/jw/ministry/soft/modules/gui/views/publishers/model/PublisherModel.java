/**
 * 
 */
package jw.ministry.soft.modules.gui.views.publishers.model;

import java.text.SimpleDateFormat;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jw.ministry.soft.modules.data.dto.Contact;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.PublisherPrivilege;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.utils.DateUtils;

/**
 * @author HervéClaude
 *
 */
public class PublisherModel {

	private Publisher publisherRecord;

	private IntegerProperty publisherIdProperty;
	private StringProperty publisherFirstNameProperty;
	private StringProperty publisherLastNameProperty;
	private StringProperty publisherSexeProperty;
	private StringProperty publisherPhoneNumberProperty;
	private StringProperty publisherEmailProperty;
	private StringProperty publisherMobileNumberProperty;
	private StringProperty publisherStreetProperty;
	private StringProperty publisherHouseNumberProperty;
	private StringProperty publisherAppartmentProperty;
	private StringProperty publisherPostalCoderProperty;
	private StringProperty publisherCityProperty;
	private StringProperty publisherCountryProperty;
	private StringProperty publisherDateOfBirthProperty;
	private StringProperty publisherStatusProperty;
	private StringProperty publisherSkypeIdProperty;
	private SimpleListProperty<String> publisherPrivileges;
	private SimpleListProperty<String> publisherTerritories;
	private StringProperty publisherGroupProperty;

	public PublisherModel(Publisher publisherRecord) {
		this.publisherIdProperty = new SimpleIntegerProperty(
				publisherRecord.getPublisherId());
		this.publisherFirstNameProperty = new SimpleStringProperty(
				publisherRecord.getFirstName());
		this.publisherLastNameProperty = new SimpleStringProperty(
				publisherRecord.getLastName());
		if (publisherRecord.getSexe() != null) {
			this.publisherSexeProperty = new SimpleStringProperty(
					publisherRecord.getSexe().getSexe());
		} else {
			this.publisherSexeProperty = new SimpleStringProperty("");
		}

		if (publisherRecord.getBirthday() != null) {
			SimpleDateFormat f = new SimpleDateFormat(
					DateUtils.getUiDateFormat());
			this.publisherDateOfBirthProperty = new SimpleStringProperty(
					f.format(publisherRecord.getBirthday()));
		} else {
			this.publisherDateOfBirthProperty = new SimpleStringProperty("");
		}

		Contact address = publisherRecord.getContact();

		if (address != null) {

			String stringValue = address.getPhone() == null ? "" : address.getPhone();
			this.publisherPhoneNumberProperty = new SimpleStringProperty(
					stringValue);
			stringValue = address.getCellPhone() == null ? "" : address.getCellPhone();
			this.publisherMobileNumberProperty = new SimpleStringProperty(
					stringValue);
			stringValue = address.getStreet() == null ? "" : address.getStreet();
			this.publisherStreetProperty = new SimpleStringProperty(
					stringValue);
			stringValue = address.getHouse() == null ? "" : address.getHouse();
			this.publisherHouseNumberProperty = new SimpleStringProperty(
					stringValue);
			stringValue = address.getPostalCode() == null ? "" : address.getPostalCode();
			this.publisherPostalCoderProperty = new SimpleStringProperty(
					stringValue);
			stringValue = address.getCity() == null ? "" : address.getCity();
			this.publisherCityProperty = new SimpleStringProperty(
					stringValue);

			if (address.getAppartment() != null) {
				this.publisherAppartmentProperty = new SimpleStringProperty(
						address.getAppartment());
			} else {
				this.publisherAppartmentProperty = new SimpleStringProperty("");
			}

			if (address.getCountry() != null) {
				this.publisherCountryProperty = new SimpleStringProperty(
						address.getCountry());
			} else {
				this.publisherCountryProperty = new SimpleStringProperty("");
			}
			
			if (address.getEmail() != null) {
				this.publisherEmailProperty = new SimpleStringProperty(
						address.getEmail());
			} else {
				this.publisherEmailProperty = new SimpleStringProperty("");
			}

			if (address.getSkypeId() != null) {
				this.publisherSkypeIdProperty = new SimpleStringProperty(
						address.getSkypeId());
			} else {
				this.publisherSkypeIdProperty = new SimpleStringProperty("");
			}

		} else {
			
			String stringValue = "";
			this.publisherPhoneNumberProperty = new SimpleStringProperty(
					stringValue);
			this.publisherMobileNumberProperty = new SimpleStringProperty(
					stringValue);
			this.publisherStreetProperty = new SimpleStringProperty(
					stringValue);
			this.publisherHouseNumberProperty = new SimpleStringProperty(
					stringValue);
			this.publisherPostalCoderProperty = new SimpleStringProperty(
					stringValue);
			this.publisherCityProperty = new SimpleStringProperty(
					stringValue);
			this.publisherAppartmentProperty = new SimpleStringProperty(stringValue);
			this.publisherCountryProperty = new SimpleStringProperty(stringValue);
			this.publisherEmailProperty = new SimpleStringProperty(stringValue);
			this.publisherSkypeIdProperty = new SimpleStringProperty(stringValue);
		}

		if (publisherRecord.getStatus() != null) {
			this.publisherStatusProperty = new SimpleStringProperty(
					publisherRecord.getStatus().getStatus());
		} else {
			this.publisherStatusProperty = new SimpleStringProperty("");
		}

		if (!publisherRecord.getPublisherPrivileges().isEmpty()) {

			ObservableList<String> stringList = FXCollections
					.observableArrayList();
			for (PublisherPrivilege priv : publisherRecord
					.getPublisherPrivileges()) {
				stringList.add(priv.getPrivilege().getPrivilege());
			}
			this.publisherPrivileges = new SimpleListProperty<String>(
					stringList);

		} else {
			this.publisherPrivileges = new SimpleListProperty<String>();
		}

		if (!publisherRecord.getTerritoriesassignmentses().isEmpty()) {

			ObservableList<String> stringList = FXCollections
					.observableArrayList();
			for (Territoriesassignments as : publisherRecord
					.getTerritoriesassignmentses()) {
				stringList.add(as.getTerritory().getCode());
			}
			this.publisherTerritories = new SimpleListProperty<String>(
					stringList);

		} else {
			this.publisherTerritories = new SimpleListProperty<String>();
		}

		if (publisherRecord.getPublishinggroup() != null) {
			this.publisherGroupProperty = new SimpleStringProperty(
					publisherRecord.getPublishinggroup().getName());
		} else {
			this.publisherGroupProperty = new SimpleStringProperty("");
		}

		this.publisherRecord = publisherRecord;
	}

	/**
	 * @return the publisherRecord
	 */
	public Publisher getPublisherRecord() {
		return publisherRecord;
	}

	/**
	 * @param publisherRecord
	 *            the publisherRecord to set
	 */
	public void setPublisherRecord(Publisher publisherRecord) {
		this.publisherRecord = publisherRecord;
	}

	/**
	 * @return the publisherIdProperty
	 */
	public IntegerProperty getPublisherIdProperty() {
		return publisherIdProperty;
	}

	/**
	 * @param publisherIdProperty
	 *            the publisherIdProperty to set
	 */
	public void setPublisherIdProperty(IntegerProperty publisherIdProperty) {
		this.publisherIdProperty = publisherIdProperty;
	}

	/**
	 * @return the publisherFirstNameProperty
	 */
	public StringProperty getPublisherFirstNameProperty() {
		return publisherFirstNameProperty;
	}

	/**
	 * @param publisherFirstNameProperty
	 *            the publisherFirstNameProperty to set
	 */
	public void setPublisherFirstNameProperty(
			StringProperty publisherFirstNameProperty) {
		this.publisherFirstNameProperty = publisherFirstNameProperty;
	}

	/**
	 * @return the publisherLastNameProperty
	 */
	public StringProperty getPublisherLastNameProperty() {
		return publisherLastNameProperty;
	}

	/**
	 * @param publisherLastNameProperty
	 *            the publisherLastNameProperty to set
	 */
	public void setPublisherLastNameProperty(
			StringProperty publisherLastNameProperty) {
		this.publisherLastNameProperty = publisherLastNameProperty;
	}

	/**
	 * @return the publisherSexeProperty
	 */
	public StringProperty getPublisherSexeProperty() {
		return publisherSexeProperty;
	}

	/**
	 * @param publisherSexeProperty
	 *            the publisherSexeProperty to set
	 */
	public void setPublisherSexeProperty(StringProperty publisherSexeProperty) {
		this.publisherSexeProperty = publisherSexeProperty;
	}

	/**
	 * @return the publisherPhoneNumberProperty
	 */
	public StringProperty getPublisherPhoneNumberProperty() {
		return publisherPhoneNumberProperty;
	}

	/**
	 * @param publisherPhoneNumberProperty
	 *            the publisherPhoneNumberProperty to set
	 */
	public void setPublisherPhoneNumberProperty(
			StringProperty publisherPhoneNumberProperty) {
		this.publisherPhoneNumberProperty = publisherPhoneNumberProperty;
	}

	/**
	 * @return the publisherMobileNumberProperty
	 */
	public StringProperty getPublisherMobileNumberProperty() {
		return publisherMobileNumberProperty;
	}

	/**
	 * @param publisherMobileNumberProperty
	 *            the publisherMobileNumberProperty to set
	 */
	public void setPublisherMobileNumberProperty(
			StringProperty publisherMobileNumberProperty) {
		this.publisherMobileNumberProperty = publisherMobileNumberProperty;
	}

	/**
	 * @return the publisherStreetProperty
	 */
	public StringProperty getPublisherStreetProperty() {
		return publisherStreetProperty;
	}

	/**
	 * @param publisherStreetProperty
	 *            the publisherStreetProperty to set
	 */
	public void setPublisherStreetProperty(
			StringProperty publisherStreetProperty) {
		this.publisherStreetProperty = publisherStreetProperty;
	}

	/**
	 * @return the publisherHouseNumberProperty
	 */
	public StringProperty getPublisherHouseNumberProperty() {
		return publisherHouseNumberProperty;
	}

	/**
	 * @param publisherHouseNumberProperty
	 *            the publisherHouseNumberProperty to set
	 */
	public void setPublisherHouseNumberProperty(
			StringProperty publisherHouseNumberProperty) {
		this.publisherHouseNumberProperty = publisherHouseNumberProperty;
	}

	/**
	 * @return the publisherPostalCoderProperty
	 */
	public StringProperty getPublisherPostalCoderProperty() {
		return publisherPostalCoderProperty;
	}

	/**
	 * @param publisherPostalCoderProperty
	 *            the publisherPostalCoderProperty to set
	 */
	public void setPublisherPostalCoderProperty(
			StringProperty publisherPostalCoderProperty) {
		this.publisherPostalCoderProperty = publisherPostalCoderProperty;
	}

	/**
	 * @return the publisherCityProperty
	 */
	public StringProperty getPublisherCityProperty() {
		return publisherCityProperty;
	}

	/**
	 * @param publisherCityProperty
	 *            the publisherCityProperty to set
	 */
	public void setPublisherCityProperty(StringProperty publisherCityProperty) {
		this.publisherCityProperty = publisherCityProperty;
	}

	/**
	 * @return the publisherEmailProperty
	 */
	public StringProperty getPublisherEmailProperty() {
		return publisherEmailProperty;
	}

	/**
	 * @param publisherEmailProperty
	 *            the publisherEmailProperty to set
	 */
	public void setPublisherEmailProperty(StringProperty publisherEmailProperty) {
		this.publisherEmailProperty = publisherEmailProperty;
	}

	/**
	 * @return the publisherAppartmentProperty
	 */
	public StringProperty getPublisherAppartmentProperty() {
		return publisherAppartmentProperty;
	}

	/**
	 * @param publisherAppartmentProperty
	 *            the publisherAppartmentProperty to set
	 */
	public void setPublisherAppartmentProperty(
			StringProperty publisherAppartmentProperty) {
		this.publisherAppartmentProperty = publisherAppartmentProperty;
	}

	/**
	 * @return the publisherCountryProperty
	 */
	public StringProperty getPublisherCountryProperty() {
		return publisherCountryProperty;
	}

	/**
	 * @param publisherCountryProperty
	 *            the publisherCountryProperty to set
	 */
	public void setPublisherCountryProperty(
			StringProperty publisherCountryProperty) {
		this.publisherCountryProperty = publisherCountryProperty;
	}

	/**
	 * @return the publisherDateOfBirthProperty
	 */
	public StringProperty getPublisherDateOfBirthProperty() {
		return publisherDateOfBirthProperty;
	}

	/**
	 * @param publisherDateOfBirthProperty
	 *            the publisherDateOfBirthProperty to set
	 */
	public void setPublisherDateOfBirthProperty(
			StringProperty publisherDateOfBirthProperty) {
		this.publisherDateOfBirthProperty = publisherDateOfBirthProperty;
	}

	/**
	 * @return the publisherStatusProperty
	 */
	public StringProperty getPublisherStatusProperty() {
		return publisherStatusProperty;
	}

	/**
	 * @param publisherStatusProperty
	 *            the publisherStatusProperty to set
	 */
	public void setPublisherStatusProperty(
			StringProperty publisherStatusProperty) {
		this.publisherStatusProperty = publisherStatusProperty;
	}

	/**
	 * @return the publisherPrivileges
	 */
	public SimpleListProperty<String> getPublisherPrivileges() {
		return publisherPrivileges;
	}

	/**
	 * @param publisherPrivileges
	 *            the publisherPrivileges to set
	 */
	public void setPublisherPrivileges(
			SimpleListProperty<String> publisherPrivileges) {
		this.publisherPrivileges = publisherPrivileges;
	}

	/**
	 * @return the publisherSkypeIdProperty
	 */
	public StringProperty getPublisherSkypeIdProperty() {
		return publisherSkypeIdProperty;
	}

	/**
	 * @param publisherSkypeIdProperty
	 *            the publisherSkypeIdProperty to set
	 */
	public void setPublisherSkypeIdProperty(
			StringProperty publisherSkypeIdProperty) {
		this.publisherSkypeIdProperty = publisherSkypeIdProperty;
	}

	/**
	 * @return the publisherTerritories
	 */
	public SimpleListProperty<String> getPublisherTerritories() {
		return publisherTerritories;
	}

	/**
	 * @param publisherTerritories
	 *            the publisherTerritories to set
	 */
	public void setPublisherTerritories(
			SimpleListProperty<String> publisherTerritories) {
		this.publisherTerritories = publisherTerritories;
	}

	/**
	 * @return the publisherGroupProperty
	 */
	public StringProperty getPublisherGroupProperty() {
		return publisherGroupProperty;
	}

	/**
	 * @param publisherGroupProperty
	 *            the publisherGroupProperty to set
	 */
	public void setPublisherGroupProperty(StringProperty publisherGroupProperty) {
		this.publisherGroupProperty = publisherGroupProperty;
	}

}
