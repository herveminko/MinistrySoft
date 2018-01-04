/**
 *
 */
package jw.ministry.soft.modules.data.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jw.ministry.soft.modules.gui.views.territories.model.TerritoryModel;

/**
 * @author HervéClaude
 *
 */
public class TerritoryReportData {

	List<TerritoryModel> lastSixMonthsWork;
	List<TerritoryModel> lastTwelveMonthsWork;
	List<TerritoryModel> longerThanAYearWork;
	List<TerritoryModel> notAssigned;
	List<TerritoryModel> neverWorked;


	public TerritoryReportData() {
		this.lastSixMonthsWork = new ArrayList<TerritoryModel>();
	    this.lastTwelveMonthsWork= new ArrayList<TerritoryModel>();
		this.longerThanAYearWork = new ArrayList<TerritoryModel>();
		this.notAssigned = new ArrayList<TerritoryModel>();
		this.neverWorked = new ArrayList<TerritoryModel>();
	}


	public List<TerritoryModel> getAllWorkedTerritories() {
		List<TerritoryModel> result = new ArrayList<TerritoryModel>();
		result.addAll(lastSixMonthsWork);
		result.addAll(lastTwelveMonthsWork);
		result.addAll(longerThanAYearWork);
		result.addAll(neverWorked);
//		result.addAll(notAssigned);

		return result;
	}

	/**
	 * @return the lastSixMonthsWork
	 */
	public List<TerritoryModel> getLastSixMonthsWork() {
		return lastSixMonthsWork;
	}
	/**
	 * @param lastSixMonthsWork the lastSixMonthsWork to set
	 */
	public void setLastSixMonthsWork(List<TerritoryModel> lastSixMonthsWork) {
		this.lastSixMonthsWork = lastSixMonthsWork;
	}
	/**
	 * @return the lastTwelveMonthsWork
	 */
	public List<TerritoryModel> getLastTwelveMonthsWork() {
		return lastTwelveMonthsWork;
	}
	/**
	 * @param lastTwelveMonthsWork the lastTwelveMonthsWork to set
	 */
	public void setLastTwelveMonthsWork(List<TerritoryModel> lastTwelveMonthsWork) {
		this.lastTwelveMonthsWork = lastTwelveMonthsWork;
	}
	/**
	 * @return the longerThanAYearWork
	 */
	public List<TerritoryModel> getLongerThanAYearWork() {
		return longerThanAYearWork;
	}
	/**
	 * @param longerThanAYearWork the longerThanAYearWork to set
	 */
	public void setLongerThanAYearWork(List<TerritoryModel> longerThanAYearWork) {
		this.longerThanAYearWork = longerThanAYearWork;
	}
	/**
	 * @return the notAssigned
	 */
	public List<TerritoryModel> getNotAssigned() {
		return notAssigned;
	}
	/**
	 * @param notAssigned the notAssigned to set
	 */
	public void setNotAssigned(List<TerritoryModel> notAssigned) {
		this.notAssigned = notAssigned;
	}


	/**
	 * @return the neverWorked
	 */
	public List<TerritoryModel> getNeverWorked() {
		return neverWorked;
	}


	/**
	 * @param neverWorked the neverWorked to set
	 */
	public void setNeverWorked(List<TerritoryModel> neverWorked) {
		this.neverWorked = neverWorked;
	}





}
