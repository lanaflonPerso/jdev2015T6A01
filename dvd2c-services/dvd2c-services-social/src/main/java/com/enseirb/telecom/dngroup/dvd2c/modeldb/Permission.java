package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the permissions database table.
 * 
 */
@Entity
@Table(name="permissions")
@NamedQuery(name="Permission.findAll", query="SELECT p FROM Permission p")
public class Permission extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String action;

	private String object;

	//bi-directional many-to-many association to Relation
	@ManyToMany
	@JoinColumn(name="id")
	private List<Role> relations;

	public Permission(String action,String object,List<Role> relations) {
		this.action=action;
		this.object=object;
		this.relations=relations;
	}
	
	public Permission() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getObject() {
		return this.object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public List<Role> getRelations() {
		return this.relations;
	}

	public void setRelations(List<Role> relations) {
		this.relations = relations;
	}

}