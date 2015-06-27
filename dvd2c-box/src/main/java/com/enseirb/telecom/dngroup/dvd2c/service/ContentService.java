package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.NoContentException;

import com.enseirb.telecom.dngroup.dvd2c.exception.AlternativeStorageException;
import com.enseirb.telecom.dngroup.dvd2c.model.Contact;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;

public interface ContentService {

	/**
	 * Verify if content exist on box and on server
	 * 
	 * @param integer
	 *            to verify
	 * @return
	 */
	public abstract boolean contentExist(Integer integer);

	/**
	 * get all contents from userID
	 * 
	 * @param userID
	 *            of get contents
	 * @return List<Content>
	 */
	public abstract List<Content> getAllContentsFromUser(UUID userID);

	/**
	 * get more information from a contentsID
	 * 
	 * @param contentsID
	 *            to get
	 * @return Content
	 * @throws NoContentException
	 */
	public abstract Content getContent(Integer contentsID)
			throws NoContentException;

	/**
	 * update Content for modification groupe or action
	 * 
	 * @param content
	 */
	public abstract void saveContent(Content content);

	/**
	 * delete a contents id
	 * 
	 * @param contentsID
	 *            to delete
	 */
	public abstract void deleteContent(Integer contentsID);

	/**
	 * is use just for change status a video
	 * 
	 * @param qUEUE_NAME
	 * @param status
	 */
	public abstract void updateContent(String qUEUE_NAME, Integer status);

	public abstract Content createContent(String userID,
			InputStream uploadedInputStream, String contentDisposition)
			throws IOException;

	public abstract Content createNewContentResolution(String contentId,
			String resolutionName, InputStream iS, String contentDisposition)
			throws AlternativeStorageException, IOException;

	public abstract void updateContentWithUrl(String contentId,
			String resolution, String url) throws NoContentException;

}