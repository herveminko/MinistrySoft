package jw.ministry.soft.modules.data.exchange.imports;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TerritoryHistoriesRecords")
@XmlAccessorType (XmlAccessType.FIELD)
public class TerritoryHistoriesRecords {

	protected String territoryName;
	protected String territoryCode;
	
	@XmlElement(name = "HistoryRecord")
	protected List<ExternalTerritoryHistory> historiesList = new ArrayList<ExternalTerritoryHistory>();
	
	
	

	/**
	 * @return the territoryName
	 */
	public String getTerritoryName() {
		return territoryName;
	}

	/**
	 * @param territoryName
	 *            the territoryName to set
	 */
	public void setTerritoryName(String territoryName) {
		this.territoryName = territoryName;
	}

	/**
	 * @return the territoryCode
	 */
	public String getTerritoryCode() {
		return territoryCode;
	}

	/**
	 * @param territoryCode
	 *            the territoryCode to set
	 */
	public void setTerritoryCode(String territoryCode) {
		this.territoryCode = territoryCode;
	}

	/**
	 * @return the historiesList
	 */
	public List<ExternalTerritoryHistory> getHistoriesList() {
		return this.historiesList;
	}

	/**
	 * @param historiesList
	 *            the historiesList to set
	 */
	public void setHistoriesList(List<ExternalTerritoryHistory> historiesList) {
		this.historiesList = historiesList;
	}

}
