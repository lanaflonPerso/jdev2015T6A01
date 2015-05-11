package com.enseirb.telecom.dngroup.snapmail.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.GoogleMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.MicrosorfMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.SMTPProperties;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;

public class MediaHomeFacadeImpl implements MediaHomeFacade {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MediaHomeFacadeImpl.class);

	@Override
	public String bodyPart2Link(InputStream inputStream, String filename, String type, String username, String password, List<String> recipients) throws IOException {
		//nh: to extract in a dedicated service class
		try {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);

			// Configuration of the client to allow a post with a large file
			//nh: please create only 1 client and close it properly
			ClientConfig cc = new ClientConfig();
			cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
			cc.property(ClientProperties.CHUNKED_ENCODING_SIZE,
					Integer.valueOf(128));
			cc.property(ClientProperties.OUTBOUND_CONTENT_LENGTH_BUFFER,
						Integer.valueOf(128));
				
			Client client = ClientBuilder.newClient(cc);
			client.register(feature).register(MultiPartFeature.class);

			//nh: create
			WebTarget target = client.target(CliConfSingleton.mediahome_host+"/api/app/"+ username + "/content");

			LOGGER.info("Filename : " + filename);
			Response response = target
					.request()
					.header("Content-Disposition", "attachment; filename=" + filename)
					.post(Entity.entity(inputStream, type), Response.class);

			if (response.getLocation() != null) {
				target = client.target(CliConfSingleton.mediahome_host+"/api/app/"+username+"/content/"+response.getLocation().toString().split("/content/")[1]);
				Content content = target.request().get(Content.class);
				
				PropertyGroups originGroups = new PropertyGroups();
				originGroups.setName("origin");
				
				Property origin = new Property();
				origin.setKey("origin");
				origin.setValue("snapmail");
				originGroups.getProps().add(origin);
				
				content.getMetadata().add(originGroups);
				
				PropertyGroups recipientsGroups = new PropertyGroups();
				recipientsGroups.setName("emails");
				
				int i = 0;
				for(String email : recipients) {
					Property p = new Property();
					p.setKey(Integer.toString(i));
					p.setValue(email);
					recipientsGroups.getProps().add(p);
					i++;
				}
				
				content.getMetadata().add(recipientsGroups);
				
				Response r = target.request().put(Entity.entity(content, MediaType.APPLICATION_JSON));
						
				return  CliConfSingleton.mediahome_host
						+ "/snapmail/"
						+ "snapmail.html#/"
						+ username
						+ "/"
						+ response.getLocation().toString().split("/content/")[1];
			}
			else {
				LOGGER.error("Error during the upload : Media@Home did not return a location");
				return "Error during the upload";
			}

		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 403) {
				LOGGER.error("Error 403 (post content)");
			} else {
				throw e;
			}
		}
		return "Error";
	}

	@Override
	public MailerProperties getSmtpParam(String username, String password) {
		try {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
			Client client = ClientBuilder.newClient();
			client.register(feature);
	
			WebTarget target = client.target(CliConfSingleton.mediahome_host + "/api/app/account/" + username);
			User user = target.request(MediaType.APPLICATION_XML_TYPE).get(User.class);
			
			for(PropertyGroups pg : user.getPropertyGroups()) {
				if(pg.getName().equals("snapmail")) {
					SMTPProperties smtpProperties = new SMTPProperties();
					for(Property p : pg.getProps()) {
						switch(p.getKey()) {
						case "google":
							if(p.getValue() != null && p.getValue() != "")
								return new GoogleMailProperties(p.getValue());
							break;
							
						case "microsoft":
							if(p.getValue() != null && p.getValue() != "")
								return new MicrosorfMailProperties(p.getValue());
							break;
							
						case "host":
							smtpProperties.setHost(p.getValue());
							break;
							
						case "port":
							smtpProperties.setPort(p.getValue());
							break;
							
						case "username":
							smtpProperties.setUsername(p.getValue());
							break;
							
						case "password":
							smtpProperties.setPassword(p.getValue());
							break;
						}
					}
					return smtpProperties;
				}
			}
			LOGGER.error("Error: there is no snapmail propertyGroups for the user "+username);
			
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 403) {
				LOGGER.error("Error 403 (getSmtpParam)");
			} else {
				throw e;
			}
		}
		// TODO: need to be changed
		return null;
	}
}
