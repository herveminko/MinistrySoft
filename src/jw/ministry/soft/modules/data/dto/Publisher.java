package jw.ministry.soft.modules.data.dto;

// Generated 18 mai 2015 18:28:53 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Publisher generated by hbm2java
 */
public class Publisher implements java.io.Serializable {

	private Integer publisherId;
	private Congregation congregation;
	private Contact contact;
	private Publishinggroup publishinggroup;
	private Sexe sexe;
	private Status status;
	private String firstName;
	private String lastName;
	private Date birthday;
	private Set<PublisherPrivilege> publisherPrivileges = new HashSet<PublisherPrivilege>(
			0);
	private Set<Territoriesassignments> territoriesassignmentses = new HashSet<Territoriesassignments>(
			0);
	private Set<Territoryhistory> territoryhistories = new HashSet<Territoryhistory>(
			0);
	private Set<Subject> subjects = new HashSet<Subject>(0);

	public Publisher() {
	}

	public Publisher(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Publisher(Congregation congregation, Contact contact,
			Publishinggroup publishinggroup, Sexe sexe, Status status,
			String firstName, String lastName, Date birthday,
			Set<PublisherPrivilege> publisherPrivileges,
			Set<Territoriesassignments> territoriesassignmentses,
			Set<Territoryhistory> territoryhistories, Set<Subject> subjects) {
		this.congregation = congregation;
		this.contact = contact;
		this.publishinggroup = publishinggroup;
		this.sexe = sexe;
		this.status = status;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.publisherPrivileges = publisherPrivileges;
		this.territoriesassignmentses = territoriesassignmentses;
		this.territoryhistories = territoryhistories;
		this.subjects = subjects;
	}

	public Integer getPublisherId() {
		return this.publisherId;
	}

	public void setPublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}

	public Congregation getCongregation() {
		return this.congregation;
	}

	public void setCongregation(Congregation congregation) {
		this.congregation = congregation;
	}

	public Contact getContact() {
		return this.contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Publishinggroup getPublishinggroup() {
		return this.publishinggroup;
	}

	public void setPublishinggroup(Publishinggroup publishinggroup) {
		this.publishinggroup = publishinggroup;
	}

	public Sexe getSexe() {
		return this.sexe;
	}

	public void setSexe(Sexe sexe) {
		this.sexe = sexe;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Set<PublisherPrivilege> getPublisherPrivileges() {
		return this.publisherPrivileges;
	}

	public void setPublisherPrivileges(
			Set<PublisherPrivilege> publisherPrivileges) {
		this.publisherPrivileges = publisherPrivileges;
	}

	public Set<Territoriesassignments> getTerritoriesassignmentses() {
		return this.territoriesassignmentses;
	}

	public void setTerritoriesassignmentses(
			Set<Territoriesassignments> territoriesassignmentses) {
		this.territoriesassignmentses = territoriesassignmentses;
	}

	public Set<Territoryhistory> getTerritoryhistories() {
		return this.territoryhistories;
	}

	public void setTerritoryhistories(Set<Territoryhistory> territoryhistories) {
		this.territoryhistories = territoryhistories;
	}

	public Set<Subject> getSubjects() {
		return this.subjects;
	}

	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}

}
