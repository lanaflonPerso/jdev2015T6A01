package com.enseirb.telecom.dngroup.dvd2c.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class CommentRepositoryMongo implements CrudRepository<CommentRepositoryObject, String>{	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentRepositoryMongo.class);


	private static final ObjectMapper mapper = null;

	@Override
	public <S extends CommentRepositoryObject> S save(S entity){
		if(entity.getCommentId() != null & exists(entity.getCommentId().toString())){
			delete(entity.getCommentId().toString());
		}
				
		try{
			MongoClient mongoClient = DbInit.Connect();
			DBCollection collection = mongoClient.getDB("mediahome").getCollection("comments");
			DBObject dbObjectToSave = DbInit.createDBObject(entity);
			
			collection.save(dbObjectToSave);
			entity.setCommentId((ObjectId)dbObjectToSave.get("_id"));
			//NHE: you should clode and open connection in the same class
			mongoClient.close();
			
		} catch (UnknownHostException e){
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			// System.err.println("Connection to database failed");
			LOGGER.error("Connection to database failed");

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			//System.err.println("Creation of dbObjectToSave failed");
			LOGGER.error("Creation of dbObjectToSave failed");
		}
		
		return entity;
	}

	@Override
	public <S extends CommentRepositoryObject> Iterable<S> save(
			Iterable<S> entities) {
		//NHE: a non implemented class should throw eg. new RuntimeException("not implemented yet")
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentRepositoryObject findOne(String id) {
		// TODO Auto-generated method stub
		
		CommentRepositoryObject comment = null;
		
		try{
			
			MongoClient mongoClient = DbInit.Connect();
			DBCollection collection = mongoClient.getDB("mediahome").getCollection("comments");
			BasicDBObject query = new BasicDBObject("_id",id);
			
			DBCursor cursor = collection.find(query);
			if(cursor.hasNext()){
				try {
					comment = mapper.readValue(cursor.next().toString(), CommentRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
					LOGGER.error("Mapping falied from Json to Java object  ");

				}
			}
						
		}catch (UnknownHostException e){
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			// System.err.println("Connection to database failed");
			LOGGER.error("Connection to database failed");

		}
		
		return comment;
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		
		boolean result = false;
		
		try{
			
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection collection = db.getCollection("comments");
			
			BasicDBObject query = new BasicDBObject("_id",id);
			DBCursor cursor = collection.find(query);
			result = cursor.hasNext();
			mongoClient.close();
			
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");


		}
		
		return result;
	}

	@Override
	public Iterable<CommentRepositoryObject> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<CommentRepositoryObject> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		
		List<CommentRepositoryObject> listOfAllComments = new ArrayList<CommentRepositoryObject>();
		Iterator<String> iterator = ids.iterator();

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			ObjectMapper mapper = new ObjectMapper();
			CommentRepositoryObject user = null;

			while (iterator.hasNext()) {
				BasicDBObject query = new BasicDBObject("userID", iterator.next());
				DBCursor cursor = dbUsers.find(query);

				while (cursor.hasNext()) {
					try {
						user = mapper.readValue(cursor.next().toString(),
								CommentRepositoryObject.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
					//	System.err.println("User Mapping failed ! ");
						LOGGER.error("User Mapping failed !");

					}

					listOfAllComments.add(user);
				}
			}

		} catch (UnknownHostException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
		//	System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

		}
		return listOfAllComments;
		
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		
		long totalNbOfComment = 0;
		
		try{
			
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection collection = db.getCollection("comments");
			
			totalNbOfComment = collection.getCount();
			mongoClient.close();
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

		}
		
		
		return totalNbOfComment ;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbComments = db.getCollection("comments");

			BasicDBObject query = new BasicDBObject("commentId", id);
			dbComments.remove(query);
			mongoClient.close();
			
		} catch (UnknownHostException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

		}
		
	}

	@Override
	public void delete(CommentRepositoryObject entity) {
		// TODO Auto-generated method stub
		
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbComments = db.getCollection("comments");
			BasicDBObject query = new BasicDBObject("commentId", entity.getCommentId());
			dbComments.remove(query);
		} catch (UnknownHostException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			 //System.err.println("Connection to database failed ");
			
			LOGGER.error("Connection to database failed");

		}
		
	}

	@Override
	public void delete(Iterable<? extends CommentRepositoryObject> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
		try{
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbComments = db.getCollection("comments");
			
			dbComments.drop();
			mongoClient.close();
			
			}
		catch(UnknownHostException e){
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			// System.err.println();
			LOGGER.error("unKnown Host");

		}
				
	}	
	
}
