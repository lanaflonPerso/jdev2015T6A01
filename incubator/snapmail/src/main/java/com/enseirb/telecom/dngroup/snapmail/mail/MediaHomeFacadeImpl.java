package com.enseirb.telecom.dngroup.snapmail.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.exception.NoSuchProperty;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.GoogleMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.MicrosoftMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.SMTPProperties;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.google.common.base.Strings;

/**
 * 
 * @author gatux
 *
 */
public class MediaHomeFacadeImpl implements MediaHomeFacade {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MediaHomeFacadeImpl.class);

	public MediaHomeFacadeImpl() {

	}

	@Override
	public String getLinkFromBodyPart(InputStream inputStream, String filename,
			String type, User user, List<String> recipients) throws IOException {
		// nh: to extract in a dedicated service class
		
		try {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature
					.basic(user.getUserID(), user.getPassword());

			// Configuration of the client to allow a post with a large file
			// nh: please create only 1 client and close it properly
			ClientConfig cc = new ClientConfig();
			cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
			cc.property(ClientProperties.CHUNKED_ENCODING_SIZE,
					Integer.valueOf(128));
			cc.property(ClientProperties.OUTBOUND_CONTENT_LENGTH_BUFFER,
					Integer.valueOf(128));

			Client client = ClientBuilder.newClient(cc);
			client.register(feature).register(MultiPartFeature.class);

			// nh: create
			// TODO: à remodifier
			WebTarget target = client.target(CliConfSingleton.mediahome_host
					+ "/api/app/content");

			LOGGER.info("Filename : " + filename);
			Response response = target
					.request()
					.header("Content-Disposition",
							"attachment; filename=" + filename)
					.post(Entity.entity(inputStream, type), Response.class);
			client.close();

			if (response.getLocation() != null) {
				/**
				 * Get the metadatas of the content
				 */
				Client client1 = ClientBuilder.newClient();
				client1.register(feature).register(MultiPartFeature.class);

				target = client1
						.target(CliConfSingleton.mediahome_host
								+ "/api/app/"
								+ "content/"
								+ response.getLocation().toString()
										.split("/content/")[1] + "/metadata");

				Content content = target
						.request(MediaType.APPLICATION_XML_TYPE).get(
								Content.class);
				client1.close();
				content.getMetadata().add("%Unreferenced");

				/**
				 * Get UUID if the recipients have a Media@Home account
				 */
				Client clientuuid = ClientBuilder.newClient();
				clientuuid.register(feature).register(MultiPartFeature.class);
				User userbymail;		
				for (String email : recipients) {
					target = clientuuid.target(CliConfSingleton.centralURL
							+ "/api/app/account/email/" + email);
					try{
					userbymail = target.request(MediaType.APPLICATION_XML_TYPE).get(User.class);
					} catch (WebApplicationException e) {
						if (e.getResponse().getStatus() == 204);
							continue;
					}
						content.getMetadata().add("%" + userbymail.getUuid());
				}
				clientuuid.close();
				// PropertyGroups originGroups = new PropertyGroups();
				// originGroups.setName("origin");
				//
				// Property origin = new Property();
				// origin.setKey("origin");
				// origin.setValue("snapmail");
				// originGroups.getProperty().add(origin);
				//
				// content.getPropertyGroups().add(originGroups);
				//
				// PropertyGroups recipientsGroups = new PropertyGroups();
				// recipientsGroups.setName("emails");
				//
				// int i = 0;
				// for (String email : recipients) {
				// Property p = new Property();
				// p.setKey(Integer.toString(i));
				// p.setValue(email);
				// recipientsGroups.getProperty().add(p);
				// i++;
				// }
				//
				// content.getPropertyGroups().add(recipientsGroups);
				Client client2 = ClientBuilder.newClient();
				client2.register(feature).register(MultiPartFeature.class);

				target = client2
						.target(CliConfSingleton.mediahome_host
								+ "/api/app/"
								+ "content/"
								+ response.getLocation().toString()
										.split("/content/")[1]);
				Response r = target.request().put(
						Entity.entity(content, MediaType.APPLICATION_XML));
				client2.close();
				return CliConfSingleton.snapmail_host
						+ "/snapmail/"
						+ "snapmail.html#/"
						+ user.getUserID()
						+ "/"
						+ response.getLocation().toString().split("/content/")[1];
			} else {
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
	public MailerProperties getMailerPropertiesFromUser(User user)
			throws NoSuchProperty {
		try {
			PropertyGroups groups;
			HttpAuthenticationFeature feature = HttpAuthenticationFeature
					.basic(user.getUserID(), user.getPassword());
			Client client = ClientBuilder.newClient();
			client.register(feature).register(MultiPartFeature.class);
			// TODO : à remodifier après sécu
			WebTarget target = client.target(CliConfSingleton.mediahome_host
					+ "/api/app/properties/Snapmail");

			groups = target.request(MediaType.APPLICATION_XML_TYPE).get(
					PropertyGroups.class);
			client.close();
			// for (PropertyGroups pg : groups) {
			// if (pg.getName().equals("snapmail")) {
			SMTPProperties smtpProperties = new SMTPProperties();
			for (Property p : groups.getProperty()) {
				switch (p.getKey()) {
				case "google":
					if (p.getValue() != null && p.getValue() != "")
						return new GoogleMailProperties(p.getValue());
					break;

				case "microsoft":
					if (p.getValue() != null && p.getValue() != "")
						return new MicrosoftMailProperties(p.getValue());
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
			if (Strings.isNullOrEmpty(smtpProperties.getUsername())
					|| Strings.isNullOrEmpty(smtpProperties.getPassword())) {
				throw new NoSuchProperty();
			}
			return smtpProperties;
			// }
			// }
			// LOGGER.error("Error: there is no snapmail propertyGroups for the user "
			// + username);

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

	@Override
	public User getUserORH(String username, String password) {
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
				username, password);
		Client client = ClientBuilder.newClient();
		client.register(feature);
		WebTarget target = client.target(CliConfSingleton.mediahome_host
				+ "/api/app/account/email/" + username);

		User user = target.request(MediaType.APPLICATION_XML_TYPE).get(
				User.class);
		user.setPassword(password);
		client.close();
		return user;
	}

}
