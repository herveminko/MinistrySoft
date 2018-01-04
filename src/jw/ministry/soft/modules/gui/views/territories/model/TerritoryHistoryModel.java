/**
 *
 */
package jw.ministry.soft.modules.gui.views.territories.model;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jw.ministry.soft.modules.data.dto.Territoryhistory;
import jw.ministry.soft.modules.utils.DateUtils;

/**
 * @author HervéClaude
 *
 */
public class TerritoryHistoryModel implements Comparable<TerritoryHistoryModel> {

	Territoryhistory historyRecord;
	private StringProperty historyActionProperty;
	private Date historyActionDate;
	private StringProperty historyActionDateProperty;
	private StringProperty historyPublisherNameProperty;
	private StringProperty historyTerritoryNameProperty;

	public TerritoryHistoryModel(Territoryhistory hist) {

		this.historyRecord = hist;

		if (this.historyRecord.getActionDate() != null) {
			SimpleDateFormat f = new SimpleDateFormat(DateUtils.getUiDateFormat());
			this.historyActionDateProperty = new SimpleStringProperty(f.format(historyRecord.getActionDate()));
			this.historyActionDate = historyRecord.getActionDate();
		}

		this.historyActionProperty = new SimpleStringProperty(historyRecord.getActionDescrption());

		this.historyPublisherNameProperty = new SimpleStringProperty(
				historyRecord.getPublisher().getFirstName() + " " + historyRecord.getPublisher().getLastName());

		this.historyTerritoryNameProperty = new SimpleStringProperty(
				historyRecord.getTerritory().getCode() + " - " + historyRecord.getTerritory().getName());

	}

	/**
	 * @return the historyRecord
	 */
	public Territoryhistory getHistoryRecord() {
		return historyRecord;
	}

	/**
	 * @param historyRecord
	 *            the historyRecord to set
	 */
	public void setHistoryRecord(Territoryhistory historyRecord) {
		this.historyRecord = historyRecord;
	}

	/**
	 * @return the historyActionProperty
	 */
	public StringProperty getHistoryActionProperty() {
		return historyActionProperty;
	}

	/**
	 * @param historyActionProperty
	 *            the historyActionProperty to set
	 */
	public void setHistoryActionProperty(StringProperty historyActionProperty) {
		this.historyActionProperty = historyActionProperty;
	}

	/**
	 * @return the historyActionDateProperty
	 */
	public StringProperty getHistoryActionDateProperty() {
		return historyActionDateProperty;
	}

	/**
	 * @param historyActionDateProperty
	 *            the historyActionDateProperty to set
	 */
	public void setHistoryActionDateProperty(StringProperty historyActionDateProperty) {
		this.historyActionDateProperty = historyActionDateProperty;
	}

	/**
	 * @return the historyPublisherNameProperty
	 */
	public StringProperty getHistoryPublisherNameProperty() {
		return historyPublisherNameProperty;
	}

	/**
	 * @param historyPublisherNameProperty
	 *            the historyPublisherNameProperty to set
	 */
	public void setHistoryPublisherNameProperty(StringProperty historyPublisherNameProperty) {
		this.historyPublisherNameProperty = historyPublisherNameProperty;
	}

	/**
	 * @return the historyTerritoryNameProperty
	 */
	public StringProperty getHistoryTerritoryNameProperty() {
		return historyTerritoryNameProperty;
	}

	/**
	 * @param historyTerritoryNameProperty
	 *            the historyTerritoryNameProperty to set
	 */
	public void setHistoryTerritoryNameProperty(StringProperty historyTerritoryNameProperty) {
		this.historyTerritoryNameProperty = historyTerritoryNameProperty;
	}

	/**
	 * @return the historyActionDate
	 */
	public Date getHistoryActionDate() {
		return historyActionDate;
	}

	/**
	 * @param historyActionDate
	 *            the historyActionDate to set
	 */
	public void setHistoryActionDate(Date historyActionDate) {
		this.historyActionDate = historyActionDate;
	}

	@Override
	public int compareTo(TerritoryHistoryModel o) {
		if (o.getHistoryActionDate().before(this.getHistoryActionDate())) {
			return -1;
		} else if (o.getHistoryActionDate().after(this.getHistoryActionDate())) {
			return 1;
		} else {
			return 0;
		}
	}

	/* Comparator for sorting the list bin ascending order */
	public static Comparator<TerritoryHistoryModel> ascendingComparator = new Comparator<TerritoryHistoryModel>() {
		public int compare(TerritoryHistoryModel h1, TerritoryHistoryModel h2) {
			if (h1.getHistoryActionDate().before(h2.getHistoryActionDate())) {
				return -1;
			} else if (h1.getHistoryActionDate().after(h2.getHistoryActionDate())) {
				return 1;
			} else {
				return 0;
			}
		}
	};

}
