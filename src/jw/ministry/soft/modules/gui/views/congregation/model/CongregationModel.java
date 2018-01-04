/**
 * 
 */
package jw.ministry.soft.modules.gui.views.congregation.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jw.ministry.soft.modules.data.dto.Congregation;

/**
 * @author HervéClaude
 *
 */
public class CongregationModel {
	
	private Congregation congregation;
	
    private IntegerProperty congregationId;
    private StringProperty congregationName;
    private StringProperty congregationLanguage;
    private StringProperty country;

    public CongregationModel() {
    }
    
    public CongregationModel(Congregation congregationRecord) {
    	this.congregationId = new SimpleIntegerProperty(congregationRecord.getCongregationId());
    	this.congregationName = new SimpleStringProperty(congregationRecord.getCongregationName());
    	this.congregationLanguage = new SimpleStringProperty(congregationRecord.getCongregationLanguage());
    	this.country = new SimpleStringProperty(congregationRecord.getContact().getCountry());
    	this.congregation = congregationRecord;
    }

	public Congregation getCongregation() {
		return congregation;
	}

	public void setCongregation(Congregation congregation) {
		this.congregation = congregation;
	}

	public IntegerProperty getCongregationId() {
		return congregationId;
	}

	public void setCongregationId(IntegerProperty congregationId) {
		this.congregationId = congregationId;
	}

	public StringProperty getCongregationName() {
		return congregationName;
	}

	public void setCongregationName(StringProperty congregationName) {
		this.congregationName = congregationName;
	}

	public StringProperty getCongregationLanguage() {
		return congregationLanguage;
	}

	public void setCongregationLanguage(StringProperty congregationLanguage) {
		this.congregationLanguage = congregationLanguage;
	}

	public StringProperty getCountry() {
		return country;
	}

	public void setCountry(StringProperty country) {
		this.country = country;
	}
    
    
}
