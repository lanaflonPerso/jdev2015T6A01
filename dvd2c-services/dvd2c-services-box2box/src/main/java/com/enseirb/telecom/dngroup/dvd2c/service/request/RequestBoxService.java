package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;


public interface RequestBoxService {

	
	/**
	 * get a box on remote host
	 * @param UserID the user to get
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchBoxException user doesn't exist on remote host 
	 */
	public abstract Box get(String boxID) throws IOException, NoSuchBoxException;

	/**
	 * post a box on remote host
	 * @param user the user to post
	 * @throws IOException host is not reachable
	 * @throws SuchBoxException user doesn't exist on remote host 
	 */
	public abstract void post(Box box) throws IOException, SuchBoxException;

	/**
	 * update a box on remote host
	 * @param user the user to update
	 * @throws IOException host is not reachable
	 * @throws NoSuchBoxException user doesn't exist on remote host 
	 */
	public abstract void put(Box box) throws IOException, NoSuchBoxException;
	
	
	
	public abstract void delete(String boxID) throws IOException, NoSuchBoxException;

	
}