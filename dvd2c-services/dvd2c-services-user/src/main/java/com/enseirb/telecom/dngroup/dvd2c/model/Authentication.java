package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the authentications database table.
 * 
 */
@Entity
@Table(name="authentications")
@NamedQuery(name="Authentication.findAll", query="SELECT a FROM Authentication a")
public class Authentication implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(length=255)
	private String provider;

	@Column(length=255)
	private String uid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public Authentication() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}