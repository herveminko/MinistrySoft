package jw.ministry.soft.modules.gui.views.territories.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.data.dto.Territoryhistory;
import jw.ministry.soft.modules.utils.DateUtils;

public class TerritoryModel {

	private Territory territory;
	private IntegerProperty territoryId;
	private StringProperty territoryName;
	private StringProperty territoryCode;
	private StringProperty territoryBounds;
	private StringProperty territoryOwnerName;
	private StringProperty territoryType;
	private StringProperty territoryRegion;
	private StringProperty territoryGroup;
	private Publisher territoryOwner;
	private byte[] territoryPicture;
	private StringProperty territoryAssignDateProperty;
	private StringProperty territoryReturnDateProperty;
	private StringProperty territoryLastWorkProperty;
	private StringProperty territoryStatusProperty;

	private List<TerritoryHistoryModel> historyData;

	public TerritoryModel(Territory territoryRecord) {
		this.territory = territoryRecord;
		this.territoryId = new SimpleIntegerProperty(territoryRecord.getTerritoryId());
		this.territoryName = new SimpleStringProperty(territoryRecord.getName());
		this.territoryCode = new SimpleStringProperty(territoryRecord.getCode());
		this.territoryBounds = new SimpleStringProperty(territoryRecord.getBounds());
		if (this.territory.getStatus() != null) {
			this.territoryStatusProperty = new SimpleStringProperty(this.territory.getStatus());
		} else {
			this.territoryStatusProperty = new SimpleStringProperty("");
		}
		if (!this.territory.getTerritoriesassignmentses().isEmpty()) {
			for (Territoriesassignments assignMent : this.territory.getTerritoriesassignmentses()) {
				Publisher pub = assignMent.getPublisher();
				if (assignMent.getAssignmentDate() != null && assignMent.getReturnDate() == null) {
					this.territoryOwner = pub;
					this.territoryOwnerName = new SimpleStringProperty(
							territoryOwner.getLastName() + " " + territoryOwner.getFirstName());

					SimpleDateFormat f = new SimpleDateFormat(DateUtils.getUiDateFormat());
					this.territoryAssignDateProperty = new SimpleStringProperty(
							f.format(assignMent.getAssignmentDate()));
					this.territoryReturnDateProperty = new SimpleStringProperty("");
					if (assignMent.getLastWorkDate() != null) {
						this.territoryLastWorkProperty = new SimpleStringProperty(
								f.format(assignMent.getLastWorkDate()));
					} else {
						this.territoryLastWorkProperty = new SimpleStringProperty("");
					}


					if (this.territory.getStatus() == null || (this.territory.getStatus() != null && !this.territory.getStatus().equalsIgnoreCase("Archivé"))) {
						this.territoryStatusProperty = new SimpleStringProperty("Travaillé");
					}


				}
			}
		} else {
			this.territoryOwnerName = new SimpleStringProperty("");
		}
		this.territoryGroup = new SimpleStringProperty(territoryRecord.getGroupName());
		this.territoryType = new SimpleStringProperty(territoryRecord.getTerritoryType());
		this.territoryRegion = new SimpleStringProperty(territoryRecord.getCity());
		this.territoryPicture = this.territory.getPicture();

		historyData = new ArrayList<TerritoryHistoryModel>();
		for (Territoryhistory history : this.territory.getTerritoryhistories()) {
			historyData.add(new TerritoryHistoryModel(history));
		}

	}

	/**
	 * @return the territory
	 */
	public Territory getTerritory() {
		return territory;
	}

	/**
	 * @param territory
	 *            the territory to set
	 */
	public void setTerritory(Territory territory) {
		this.territory = territory;
	}

	/**
	 * @return the territoryId
	 */
	public IntegerProperty getTerritoryId() {
		return territoryId;
	}

	/**
	 * @param territoryId
	 *            the territoryId to set
	 */
	public void setTerritoryId(IntegerProperty territoryId) {
		this.territoryId = territoryId;
	}

	/**
	 * @return the territoryName
	 */
	public StringProperty getTerritoryName() {
		return territoryName;
	}

	/**
	 * @param territoryName
	 *            the territoryName to set
	 */
	public void setTerritoryName(StringProperty territoryName) {
		this.territoryName = territoryName;
	}

	/**
	 * @return the territoryCode
	 */
	public StringProperty getTerritoryCode() {
		return territoryCode;
	}

	/**
	 * @param territoryCode
	 *            the territoryCode to set
	 */
	public void setTerritoryCode(StringProperty territoryCode) {
		this.territoryCode = territoryCode;
	}

	/**
	 * @return the territoryBounds
	 */
	public StringProperty getTerritoryBounds() {
		return territoryBounds;
	}

	/**
	 * @param territoryBounds
	 *            the territoryBounds to set
	 */
	public void setTerritoryBounds(StringProperty territoryBounds) {
		this.territoryBounds = territoryBounds;
	}

	/**
	 * @return the territoryOwner
	 */
	public Publisher getTerritoryOwner() {
		return territoryOwner;
	}

	/**
	 * @param territoryOwner
	 *            the territoryOwner to set
	 */
	public void setTerritoryOwner(Publisher territoryOwner) {
		this.territoryOwner = territoryOwner;
	}

	/**
	 * @return the territoryPicture
	 */
	public byte[] getTerritoryPicture() {
		return territoryPicture;
	}

	/**
	 * @param territoryPicture
	 *            the territoryPicture to set
	 */
	public void setTerritoryPicture(byte[] territoryPicture) {
		this.territoryPicture = territoryPicture;
	}

	/**
	 * @return the territoryOwnerName
	 */
	public StringProperty getTerritoryOwnerName() {
		return territoryOwnerName;
	}

	/**
	 * @param territoryOwnerName
	 *            the territoryOwnerName to set
	 */
	public void setTerritoryOwnerName(StringProperty territoryOwnerName) {
		this.territoryOwnerName = territoryOwnerName;
	}

	/**
	 * @return the territoryType
	 */
	public StringProperty getTerritoryType() {
		return territoryType;
	}

	/**
	 * @param territoryType
	 *            the territoryType to set
	 */
	public void setTerritoryType(StringProperty territoryType) {
		this.territoryType = territoryType;
	}

	/**
	 * @return the territoryRegion
	 */
	public StringProperty getTerritoryRegion() {
		return territoryRegion;
	}

	/**
	 * @param territoryRegion
	 *            the territoryRegion to set
	 */
	public void setTerritoryRegion(StringProperty territoryRegion) {
		this.territoryRegion = territoryRegion;
	}

	/**
	 * @return the territoryGroup
	 */
	public StringProperty getTerritoryGroup() {
		return territoryGroup;
	}

	/**
	 * @param territoryGroup
	 *            the territoryGroup to set
	 */
	public void setTerritoryGroup(StringProperty territoryGroup) {
		this.territoryGroup = territoryGroup;
	}

	/**
	 * @return the territoryAssignDateProperty
	 */
	public StringProperty getTerritoryAssignDateProperty() {
		return territoryAssignDateProperty;
	}

	/**
	 * @param territoryAssignDateProperty
	 *            the territoryAssignDateProperty to set
	 */
	public void setTerritoryAssignDateProperty(StringProperty territoryAssignDateProperty) {
		this.territoryAssignDateProperty = territoryAssignDateProperty;
	}

	/**
	 * @return the territoryReturnDateProperty
	 */
	public StringProperty getTerritoryReturnDateProperty() {
		return territoryReturnDateProperty;
	}

	/**
	 * @param territoryReturnDateProperty
	 *            the territoryReturnDateProperty to set
	 */
	public void setTerritoryReturnDateProperty(StringProperty territoryReturnDateProperty) {
		this.territoryReturnDateProperty = territoryReturnDateProperty;
	}

	/**
	 * @return the territoryLastWorkProperty
	 */
	public StringProperty getTerritoryLastWorkProperty() {
		return territoryLastWorkProperty;
	}

	/**
	 * @param territoryLastWorkProperty
	 *            the territoryLastWorkProperty to set
	 */
	public void setTerritoryLastWorkProperty(StringProperty territoryLastWorkProperty) {
		this.territoryLastWorkProperty = territoryLastWorkProperty;
	}

	/**
	 * @return the historyData
	 */
	public List<TerritoryHistoryModel> getHistoryData() {
		return historyData;
	}

	/**
	 * @param historyData
	 *            the historyData to set
	 */
	public void setHistoryData(List<TerritoryHistoryModel> historyData) {
		this.historyData = historyData;
	}

	/**
	 * @return the territoryStatusProperty
	 */
	public StringProperty getTerritoryStatusProperty() {
		return territoryStatusProperty;
	}

	/**
	 * @param territoryStatusProperty
	 *            the territoryStatusProperty to set
	 */
	public void setTerritoryStatusProperty(StringProperty territoryStatusProperty) {
		this.territoryStatusProperty = territoryStatusProperty;
	}

}
