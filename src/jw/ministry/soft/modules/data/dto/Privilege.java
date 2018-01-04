package jw.ministry.soft.modules.data.dto;

// Generated 18 mai 2015 18:28:53 by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Privilege generated by hbm2java
 */
public class Privilege implements java.io.Serializable {

	private Integer privilegeId;
	private String privilege;
	private Set<PublisherPrivilege> publisherPrivileges = new HashSet<PublisherPrivilege>(
			0);

	public Privilege() {
	}

	public Privilege(String privilege) {
		this.privilege = privilege;
	}

	public Privilege(String privilege,
			Set<PublisherPrivilege> publisherPrivileges) {
		this.privilege = privilege;
		this.publisherPrivileges = publisherPrivileges;
	}

	public Integer getPrivilegeId() {
		return this.privilegeId;
	}

	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getPrivilege() {
		return this.privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public Set<PublisherPrivilege> getPublisherPrivileges() {
		return this.publisherPrivileges;
	}

	public void setPublisherPrivileges(
			Set<PublisherPrivilege> publisherPrivileges) {
		this.publisherPrivileges = publisherPrivileges;
	}

}
