package jw.ministry.soft.modules.data.exchange.imports;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "TerritoryHistory")
public class ExternalTerritoryHistory {

	protected String pubisherName;
	protected String actionDateString;
	protected String actionName;

	/**
	 * @return the pubisherName
	 */
	public String getPubisherName() {
		return pubisherName;
	}

	/**
	 * @param pubisherName
	 *            the pubisherName to set
	 */
	public void setPubisherName(String pubisherName) {
		this.pubisherName = pubisherName;
	}

	/**
	 * @return the actionDateString
	 */
	public String getActionDateString() {
		return actionDateString;
	}

	/**
	 * @param actionDateString
	 *            the actionDateString to set
	 */
	public void setActionDateString(String actionDateString) {
		this.actionDateString = actionDateString;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName
	 *            the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

}
