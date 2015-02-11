package com.enseirb.telecom.dngroup.dvd2c.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UserRepositoryMongo implements UserRepository {

	private String dbName;

	public UserRepositoryMongo(String dbName) {
		this.dbName=dbName;
	}

	@Override
	public boolean exists(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");
	
			BasicDBObject query = new BasicDBObject("userID", id);
			DBCursor cursor = dbUsers.find(query);
	
			if (cursor.hasNext()) {
				mongoClient.close();
				return true;
			} else {
				mongoClient.close();
				return false;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			System.err.println("Connection to database failed ");
			return true;
		}
	}

	@Override
	public <S extends UserRepositoryObject> S save(S entity) {
		if (exists(entity.getUserID())) {
			entity = update(entity);
		} else {

			try {

				MongoClient mongoClient = DbInit.Connect();
				DB db = mongoClient.getDB(dbName);
				DBCollection dbUsers = db.getCollection("users");

				dbUsers.save(DbInit.createDBObject(entity));
				mongoClient.close();
			} catch (UnknownHostException | JsonProcessingException e) {
				// TODO Auto-generated catch block
				//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
				return null;
			}
		}

		return entity;
	}

public <S extends UserRepositoryObject> S update (S entity){
		
		MongoClient mongoClient;
		try {
			mongoClient = DbInit.Connect();

			DB db = mongoClient.getDB(dbName);
			DBCollection dbBox = db.getCollection("users");
			BasicDBObject newDocument = new BasicDBObject();

			if (entity.getUserID() != null) {
				newDocument.append("$set",new BasicDBObject().append("userID", entity.getUserID()));
				BasicDBObject searchQuery = new BasicDBObject().append("userID",entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getBoxID() != null) {
				newDocument.append("$set", new BasicDBObject().append("boxID",entity.getBoxID()));
				BasicDBObject searchQuery = new BasicDBObject().append("userID",entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getSurname() != null) {// need to verify
				newDocument.append("$set",new BasicDBObject().append("surname", entity.getSurname()));
				BasicDBObject searchQuery = new BasicDBObject().append("userID",entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getName() != null) {
				newDocument.append("$set",new BasicDBObject().append("name", entity.getName()));
				BasicDBObject searchQuery = new BasicDBObject().append("userID",entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPassword() != null) {
				newDocument.append("$set",new BasicDBObject().append("password", entity.getPassword()));
				BasicDBObject searchQuery = new BasicDBObject().append("userID",entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPubKey() != null) {
				newDocument.append("$set",new BasicDBObject().append("pubKey", entity.getPubKey()));
				BasicDBObject searchQuery = new BasicDBObject().append("userID",entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPrivateKey() != null) {
				newDocument.append("$set",new BasicDBObject().append("privateKey", entity.getPrivateKey()));
				BasicDBObject searchQuery = new BasicDBObject().append("userID",entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}

			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
		}

		return entity;
	}
	
	@Override
	public UserRepositoryObject findOne(String id) {
		// The id is the email address
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");

//			BasicDBObject query = new BasicDBObject("userID", id);
			BasicDBObject query = new BasicDBObject();
			query.put("userID",  Pattern.compile(id , Pattern.CASE_INSENSITIVE));
			DBCursor cursor = dbUsers.find(query);
			UserRepositoryObject user = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					user = mapper.readValue(cursor.next().toString(), UserRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
					System.err.println("User Mapping failed ! ");
				}
			}
			return user;

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			return null;
		}
	}

	@Override
	public Iterable<UserRepositoryObject> findAll() {
		//Iterable <UserRepositoryObject> listOfAllUsers = null;
		List <UserRepositoryObject> listOfAllUsers = new ArrayList<UserRepositoryObject>();
		
		try{
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");
			
			ObjectMapper mapper = new ObjectMapper();
			UserRepositoryObject user = null;
			
			DBCursor cursor = dbUsers.find();
					
			while(cursor.hasNext()){
				try {
					user = mapper.readValue(cursor.next().toString(), UserRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
					System.err.println("User Mapping failed ! ");
				}
				
				listOfAllUsers.add(user);
			}
			
		}
		catch (UnknownHostException e){
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			System.err.println("Connection to database failed ");			
		}
		return listOfAllUsers;
		
		//throw new RuntimeException("not yet invented");
	}

	@Override
	public Iterable<UserRepositoryObject> findAll(Iterable<String> ids) {
		// throw new RuntimeException("not yet invented");

		List<UserRepositoryObject> listOfAllUsers = new ArrayList<UserRepositoryObject>();
		Iterator<String> iterator = ids.iterator();

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");

			ObjectMapper mapper = new ObjectMapper();
			UserRepositoryObject user = null;

			while (iterator.hasNext()) {
				BasicDBObject query = new BasicDBObject("userID",
						iterator.next());
				DBCursor cursor = dbUsers.find(query);

				while (cursor.hasNext()) {
					try {
						user = mapper.readValue(cursor.next().toString(),
								UserRepositoryObject.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
						System.err.println("User Mapping failed ! ");
					}

					listOfAllUsers.add(user);
				}

			}

		} catch (UnknownHostException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			System.err.println("Connection to database failed ");
		}
		return listOfAllUsers;

	}

	@Override
	public long count() {

		// throw new RuntimeException("not yet invented");

		long numberOfUser = 0;

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");

			DBCursor cursor = dbUsers.find();
			try {
				mongoClient.close();
				while (cursor.hasNext()) {
					numberOfUser++;
				}
			} finally {
				cursor.close();
			}
		}

		catch (UnknownHostException e) {

		}

		return numberOfUser;

	}

	@Override
	public void delete(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	@Override
	public void delete(UserRepositoryObject entity) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");
			BasicDBObject query = new BasicDBObject("userID", entity.getUserID());
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	@Override
	public void delete(Iterable<? extends UserRepositoryObject> entities) {
		throw new RuntimeException("not yet invented");

	}

	@Override
	public void deleteAll() {
		
		//throw new RuntimeException("not yet invented");

		try{
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("users");
			
			dbUsers.drop();
			mongoClient.close();
			
			}
		catch(UnknownHostException e){
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			System.err.println();
		}
	}

	@Override
	public <S extends UserRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		//throw new RuntimeException("not yet invented");
		
		List<S> listOfAllUsers = new ArrayList<S>();
		Iterator<S> iterator = entities.iterator();
		
		while(iterator.hasNext()){
			if(exists(iterator.next().getUserID())){
				delete(iterator.next().getUserID());
			}
			
			try{
				MongoClient mongoClient = DbInit.Connect();
				DB db = mongoClient.getDB(dbName);
				DBCollection dbUsers = db.getCollection("user");
				
				try {
					dbUsers.save(DbInit.createDBObject(iterator.next()));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
				}
			}
			catch(UnknownHostException e){
				//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
				System.err.println("Connection to database failed ");
				
			}
		}
				
		return listOfAllUsers ;
		
	}

	@Override
	public BoxRepositoryObject findBoxFromUserID(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

}
