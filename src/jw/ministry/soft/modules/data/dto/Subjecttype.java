package jw.ministry.soft.modules.data.dto;

// Generated 24 f�vr. 2015 07:54:37 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Subjecttype generated by hbm2java
 */
public class Subjecttype implements java.io.Serializable {

	private Integer subjectTypeId;
	private String subjectName;
	private Date subjectFixedDuration;
	private Set<Subject> subjects = new HashSet<Subject>(0);

	public Subjecttype() {
	}

	public Subjecttype(String subjectName, Date subjectFixedDuration) {
		this.subjectName = subjectName;
		this.subjectFixedDuration = subjectFixedDuration;
	}

	public Subjecttype(String subjectName, Date subjectFixedDuration,
			Set<Subject> subjects) {
		this.subjectName = subjectName;
		this.subjectFixedDuration = subjectFixedDuration;
		this.subjects = subjects;
	}

	public Integer getSubjectTypeId() {
		return this.subjectTypeId;
	}

	public void setSubjectTypeId(Integer subjectTypeId) {
		this.subjectTypeId = subjectTypeId;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Date getSubjectFixedDuration() {
		return this.subjectFixedDuration;
	}

	public void setSubjectFixedDuration(Date subjectFixedDuration) {
		this.subjectFixedDuration = subjectFixedDuration;
	}

	public Set<Subject> getSubjects() {
		return this.subjects;
	}

	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}

}
