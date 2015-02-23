package com.enseirb.telecom.dngroup.dvd2c.service.request;


import java.io.IOException;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface RequestUserService {

	
	
	/**
	 * get a user on remote host
	 * @param UserID the user to get
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract User get(String string) throws IOException, NoSuchUserException;

	/**
	 * Get a list of user by name on remote host (server normally)
	 * @param name the name to request
	 * @return the list of user with this name
	 * @throws IOException the host is not reachable
	 */
	public abstract List<User> getUserFromName(String name) throws IOException;

	/**
	 * post a user on remote host
	 * @param user the user to post
	 * @throws IOException host is not reachable
	 * @throws SuchUserException user doesn't exist on remote host 
	 */
	public abstract void createUserORH(User user) throws IOException, SuchUserException;

	/**
	 * update a user on remote host
	 * @param user the user to update
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void updateUserORH(User user) throws IOException, NoSuchUserException;

	/**
	 * delete a user on remote host
	 * @param user the user to delete
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void deleteUserORH(String userID) throws IOException, NoSuchUserException;

	/**
	 * Get a box with a userID on Remote host (normally server)
	 * @param userID the userID to found the box
	 * @return the box with addr of this
	 * @throws IOException the host is not reachable
	 * @throws NoSuchBoxException no box found
	 */
	public abstract Box getBoxByUserIDORH(String userID) throws IOException, NoSuchBoxException;

}