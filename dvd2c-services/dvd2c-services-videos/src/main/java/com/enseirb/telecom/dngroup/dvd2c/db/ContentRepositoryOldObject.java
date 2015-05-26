package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.enseirb.telecom.dngroup.dvd2c.model.Comment;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;

@Entity
public class ContentRepositoryOldObject {
	@Id
	@Column(unique = true)
	String id;

	String name;
	String type;
	String actorID;
	Long unixTime;
	String link;
	String previewLink;
	String status;
//	List<Comment> comment;
//	List<String> metadata;



	public ContentRepositoryOldObject() {
	}

	public ContentRepositoryOldObject(String id, String name, String type, String actorID, Long unixTime, String link, String previewLink, String status, List<Comment> comment, List<String> metadata) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.actorID = actorID;
		this.unixTime = unixTime;
		this.link = link;
		this.previewLink = previewLink;
		this.status = status;
//		this.comment = comment;
//		this.metadata = metadata;
	}

	public ContentRepositoryOldObject(Content content) {
		id = content.getContentsID();
		name = content.getName();
		type = content.getType();
		actorID = content.getActorID();
		unixTime = content.getUnixTime();
		link = content.getLink();
		previewLink = content.getPreviewLink();
		status = content.getStatus();
//		this.comment = content.getComment();
//		this.metadata = content.getMetadata();
	
	}

//	/**
//	 * @return the metadata
//	 */
//	public List<String> getMetadata() {
//		return metadata;
//	}
//
//	/**
//	 * @param metadata the metadata to set
//	 */
//	public void setMetadata(List<String> metadata) {
//		this.metadata = metadata;
//	}

	/**
	 * @param actorID the actorID to set
	 */
	public void setActorID(String actorID) {
		this.actorID = actorID;
	}

	public String getActorID() {
		return actorID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUnixTime() {
		return unixTime;
	}

	public void setUnixTime(Long unixTime) {
		this.unixTime = unixTime;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

//	public List<Comment> getComment() {
//		return comment;
//	}
//
//	public void setComment(List<Comment> comment) {
//		this.comment = comment;
//	}


	public String getPreviewLink() {
		return previewLink;
	}
	public void setPreviewLink(String previewLink) {
		this.previewLink = previewLink;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Content toContent() {
		Content content = new Content();
		content.setContentsID(this.getId().toString());
		content.setName(this.getName());
		content.setType(this.getType());
		content.setActorID(this.getActorID());
		content.setLink(this.getLink());
		content.setPreviewLink(this.getPreviewLink());
		content.setUnixTime(this.getUnixTime());
		content.setStatus(this.getStatus());
//		content.getComment().addAll(this.getComment());
//		content.getMetadata().addAll(this.getMetadata());
		return content;
	}


	

}