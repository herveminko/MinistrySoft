/**
 *
 */
package jw.ministry.soft.modules.data.reports;

import java.util.Arrays;

import jw.ministry.soft.modules.gui.views.territories.model.TerritoryModel;

/**
 * @author HervéClaude
 *
 */
public class DetailledGroupTerritoryReportData {
	String groupName;
	TypedGroupTerritoryReportData studentsTerritoriesReports;
	TypedGroupTerritoryReportData refugeesTerritoriesReports;
	TypedGroupTerritoryReportData researchTerritoriesReports;

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the studentsTerritoriesReports
	 */
	public TypedGroupTerritoryReportData getStudentsTerritoriesReports() {
		return studentsTerritoriesReports;
	}

	/**
	 * @param studentsTerritoriesReports
	 *            the studentsTerritoriesReports to set
	 */
	public void setStudentsTerritoriesReports(TypedGroupTerritoryReportData studentsTerritoriesReports) {
		this.studentsTerritoriesReports = studentsTerritoriesReports;
	}

	/**
	 * @return the refugeesTerritoriesReports
	 */
	public TypedGroupTerritoryReportData getRefugeesTerritoriesReports() {
		return refugeesTerritoriesReports;
	}

	/**
	 * @param refugeesTerritoriesReports
	 *            the refugeesTerritoriesReports to set
	 */
	public void setRefugeesTerritoriesReports(TypedGroupTerritoryReportData refugeesTerritoriesReports) {
		this.refugeesTerritoriesReports = refugeesTerritoriesReports;
	}

	/**
	 * @return the researchTerritoriesReports
	 */
	public TypedGroupTerritoryReportData getResearchTerritoriesReports() {
		return researchTerritoriesReports;
	}

	/**
	 * @param researchTerritoriesReports
	 *            the researchTerritoriesReports to set
	 */
	public void setResearchTerritoriesReports(TypedGroupTerritoryReportData researchTerritoriesReports) {
		this.researchTerritoriesReports = researchTerritoriesReports;
	}

	/* Overall methods. */

	public int getLastSixMonthWorkedTerritoriesCount() {
		int count = 0;

		for (TypedGroupTerritoryReportData reportsData : Arrays.asList(getStudentsTerritoriesReports(),
				getRefugeesTerritoriesReports(), getResearchTerritoriesReports())) {
			count += reportsData.getLastSixMonthsWork().size();
		}
		return count;
	}

	public int getLastTwelveMonthWorkedTerritoriesCount() {
		int count = 0;

		for (TypedGroupTerritoryReportData reportsData : Arrays.asList(getStudentsTerritoriesReports(),
				getRefugeesTerritoriesReports(), getResearchTerritoriesReports())) {
			count += reportsData.getLastTwelveMonthsWork().size();
		}

		return count;
	}

	public int getLongerThanAYearWorkedTerritoriesCount() {
		int count = 0;

		for (TypedGroupTerritoryReportData reportsData : Arrays.asList(getStudentsTerritoriesReports(),
				getRefugeesTerritoriesReports(), getResearchTerritoriesReports())) {
			count += reportsData.getLongerThanAYearWork().size();
		}

		return count;
	}

	public int getNotAssignedTerritoriesCount() {
		int count = 0;
		for (TypedGroupTerritoryReportData reportsData : Arrays.asList(getStudentsTerritoriesReports(),
				getRefugeesTerritoriesReports(), getResearchTerritoriesReports())) {
			count += reportsData.getNotAssigned().size();
		}

		return count;
	}

	public int getNeverWorkedTerritoriesCount() {
		int count = 0;

		for (TypedGroupTerritoryReportData reportsData : Arrays.asList(getStudentsTerritoriesReports(),
				getRefugeesTerritoriesReports(), getResearchTerritoriesReports())) {
			count += reportsData.getNeverWorked().size();
		}

		return count;
	}

	public int getRefugeesTerritoriesAssignedToGroup() {
		int groupTerritoriesCount = 0;

		for (TerritoryModel ter: getRefugeesTerritoriesReports().getAllWorkedTerritories()) {
			if (ter.getTerritoryOwnerName() != null && ter.getTerritoryOwnerName().getValue().contains("Groupe")) {
				groupTerritoriesCount++;
			}
		}

		return groupTerritoriesCount;
	}

	public int getStudentTerritoriesAssignedToGroup() {
		int groupTerritoriesCount = 0;

		for (TerritoryModel ter: getStudentsTerritoriesReports().getAllWorkedTerritories()) {
			if (ter.getTerritoryOwnerName() != null && ter.getTerritoryOwnerName().getValue().contains("Groupe")) {
				groupTerritoriesCount++;
			}

		}

		return groupTerritoriesCount;
	}


	public int getSearchTerritoriesAssignedToGroup() {
		int groupTerritoriesCount = 0;

		for (TerritoryModel ter: getResearchTerritoriesReports().getAllWorkedTerritories()) {
			if (ter.getTerritoryOwnerName() != null && ter.getTerritoryOwnerName().getValue().contains("Groupe")) {
				groupTerritoriesCount++;
			}
//			if (ter.getTerritoryOwnerName() != null ) {
//			System.out.println(ter.getTerritoryCode() + " ---- " + ter.getTerritoryOwnerName().getValue());
//			}
		}

		return groupTerritoriesCount;
	}




}
