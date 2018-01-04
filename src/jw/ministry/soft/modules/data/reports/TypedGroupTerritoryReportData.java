/**
 *
 */
package jw.ministry.soft.modules.data.reports;

import java.util.Map;

import jw.ministry.soft.modules.gui.views.territories.model.TerritoryModel;

/**
 * @author HervéClaude
 *
 */
public class TypedGroupTerritoryReportData extends TerritoryReportData {
	private static String searchTypeString;

	private TerritoryReportData groupCompleteData;

	public TypedGroupTerritoryReportData(String searchTypeString, TerritoryReportData groupCompleteData) {

		this.searchTypeString = searchTypeString;
		this.groupCompleteData = groupCompleteData;

		for(TerritoryModel model: groupCompleteData.getLastSixMonthsWork()) {
			if (model.getTerritoryType().getValue().contains(searchTypeString)) {
				this.getLastSixMonthsWork().add(model);
			}
		}

		for(TerritoryModel model: groupCompleteData.getLastTwelveMonthsWork()) {
			if (model.getTerritoryType().getValue().contains(searchTypeString)) {
				this.getLastTwelveMonthsWork().add(model);
			}
		}

		for(TerritoryModel model: groupCompleteData.getLongerThanAYearWork()) {
			if (model.getTerritoryType().getValue().contains(searchTypeString)) {
				this.getLongerThanAYearWork().add(model);
			}
		}

		for(TerritoryModel model: groupCompleteData.getNeverWorked()) {
			if (model.getTerritoryType().getValue().contains(searchTypeString)) {
				this.getNeverWorked().add(model);
			}
		}

		for(TerritoryModel model: groupCompleteData.getNotAssigned()) {
			if (model.getTerritoryType().getValue().contains(searchTypeString)) {
				this.getNotAssigned().add(model);
			}
		}

	}

	/**
	 * @return the searchTypeString
	 */
	public static String getSearchTypeString() {
		return searchTypeString;
	}

	/**
	 * @param searchTypeString the searchTypeString to set
	 */
	public static void setSearchTypeString(String searchTypeString) {
		TypedGroupTerritoryReportData.searchTypeString = searchTypeString;
	}

	/**
	 * @return the groupCompleteData
	 */
	public TerritoryReportData getGroupCompleteData() {
		return groupCompleteData;
	}







}
