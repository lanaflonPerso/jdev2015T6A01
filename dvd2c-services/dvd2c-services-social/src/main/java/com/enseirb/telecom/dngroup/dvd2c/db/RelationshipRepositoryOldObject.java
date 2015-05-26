package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;

@Entity
public class RelationshipRepositoryOldObject {

	@Id
	String iD;
	String userId;
	String actorID;
	String firstname;
	String surname;
	String pubKey;
	Integer aprouve;
	Long unixTime;
	
//	List<String> role;

	public RelationshipRepositoryOldObject() {
	}

	public RelationshipRepositoryOldObject(String iD, String userId,
			String actorID, String firstname, String surname, String pubKey,
			Integer aprouve, Long unixTime, List<String> role) {
		super();
		this.iD = userId + actorID;
		this.userId = userId;
		this.actorID = actorID;
		this.firstname = firstname;
		this.surname = surname;
		this.pubKey = pubKey;
		this.aprouve = aprouve;
		this.unixTime = unixTime;
//		this.role = role;

	}

	public RelationshipRepositoryOldObject(String userId, String actorID,
			String firstname, String surname, String pubKey, Integer aprouve,
			Long unixTime, List<String> role) {
		super();
		this.iD = userId + actorID;
		this.userId = userId;
		this.actorID = actorID;
		this.firstname = firstname;
		this.surname = surname;
		this.pubKey = pubKey;
		this.aprouve = aprouve;
		this.unixTime = unixTime;
//		this.role = role;

	}

	public RelationshipRepositoryOldObject(String userId, ContactXSD relation) {
		this.iD = userId + relation.getActorID();
		this.userId = userId;
		this.actorID = relation.getActorID();
		this.firstname = relation.getFirstname();
		this.surname = relation.getSurname();
		this.pubKey = relation.getPubKey();
		this.aprouve = relation.getAprouve();
		this.unixTime = relation.getUnixTime();
//		this.role = relation.getRole();

	}

	public String getID() {
		return iD;
	}

	public void setID(String iD) {
		this.iD = iD;
	}

	/**
	 * @return the role
	 */
//	public List<String> getRole() {
//		return role;
//	}

	/**
	 * @param role
	 *            the role to set
	 */
//	public void setRole(List<String> role) {
//		this.role = role;
//	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		this.iD = userId + actorID;
	}

	public String getActorID() {
		return actorID;
	}

	public void setActorID(String actorID) {
		this.actorID = actorID;
		this.iD = userId + actorID;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPubKey() {
		return pubKey;
	}

	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	public Integer getAprouve() {
		return aprouve;
	}

	public void setAprouve(Integer aprouve) {
		this.aprouve = aprouve;
	}

	public Long getUnixTime() {
		return unixTime;
	}

	public void setUnixTime(Long unixTime) {
		this.unixTime = unixTime;
	}

	public ContactXSD toRelation() {
		ContactXSD relation = new ContactXSD();
		relation.setActorID(actorID);
		relation.setFirstname(firstname);
		relation.setSurname(surname);
		relation.setPubKey(pubKey);
		relation.setUnixTime(unixTime);
		relation.setAprouve(aprouve);
//		relation.getRole().addAll(role);
		return relation;

	}
}