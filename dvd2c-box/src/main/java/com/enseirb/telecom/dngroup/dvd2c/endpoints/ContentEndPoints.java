package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQServiceImpl;
import com.google.common.io.Files;

// The Java class will be hosted at the URI path "/app/content"
@Path("app/{userID}/content")
public class ContentEndPoints {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentEndPoints.class);

	@Autowired
	protected ContentService uManager = null;

	// ContentService uManager = new ContentServiceImpl(
	// new ContentRepositoryMongo(), new RabbitMQServer());

	/**
	 * Get all contents for a user. This request only called by videos owners
	 * 
	 * @param userID
	 * @return Content list
	 */
	@GET
	@RolesAllowed("other")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Content> getAllContentsFromUser(
			@PathParam("userID") String userID) {
		List<Content> contents = uManager.getAllContentsFromUser(userID);
		return contents;
	}

	/**
	 * Get a specific content from the owner
	 * 
	 * @param userID
	 * @return Content list
	 */
	@GET
	@Path("{contentsID}/metadata")
	@RolesAllowed({ "authenticated", "other" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Content getContentMetadata(@PathParam("userID") String userID,
			@PathParam("contentsID") String contentsID) {
		Content content = uManager.getContent(contentsID);
		if (content.getActorID().equals(userID)) {
			content.setLink(CliConfSingleton.publicAddr + content.getLink());
			return content;
		} else {
			// No URL parameter idLanguage was sent
			ResponseBuilder builder = Response
					.status(Response.Status.FORBIDDEN);
			builder.entity("This content doesn't belong to you ! ");
			Response response = builder.build();
			throw new WebApplicationException(response);
		}

	}

	/**
	 * Get a specific content from the owner
	 * 
	 * @param userID
	 * @return Content list
	 * @throws URISyntaxException
	 */
	@GET
	@Path("{contentsID}")
	@RolesAllowed({ "authenticated", "other" })
	@Produces({ MediaType.WILDCARD })
	public Response getContent(@PathParam("userID") String userID,
			@PathParam("contentsID") String contentsID)
			throws URISyntaxException {
		Content content = uManager.getContent(contentsID);
		if (content.getActorID().equals(userID)) {
			URI uri = new URI(CliConfSingleton.publicAddr + content.getLink()
					+ "/" + content.getName());
			return Response.seeOther(uri).build();
		} else {
			// No URL parameter idLanguage was sent
			ResponseBuilder builder = Response
					.status(Response.Status.FORBIDDEN);
			builder.entity("This content doesn't belong to you ! ");
			Response response = builder.build();
			throw new WebApplicationException(response);
		}

	}

	// @POST
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public Response postVideo(Content content) {
	// return Response.status(Status.SERVICE_UNAVAILABLE).build();
	// }

	/**
	 * post a file on the box for the userID
	 * 
	 * @param userID
	 *            the sender of the request
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param body
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@POST
	@RolesAllowed({ "other", "authenticated" })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postContent(@PathParam("userID") String userID,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") FormDataBodyPart body)
			throws URISyntaxException, IOException {

		String fileName = fileDetail.getFileName();
		LOGGER.info("New file {}", fileDetail);
		String extension = Files.getFileExtension(fileName);
		MediaType fileMediaType = body.getMediaType();
		String fileTypeTemp = fileMediaType.toString();
		String[] fileType = fileTypeTemp.split("/");

		File upload = File.createTempFile(userID, "." + extension,
				Files.createTempDir());
		Content content = uManager.createContent(userID, uploadedInputStream,
				fileType, upload);
		// content.setLink(CliConfSingleton.publicAddr + content.getLink());
		// return content;
		// return Response.created(new
		// URI("app/"+userID+"/content/"+content.getContentsID())).build();
		return Response.created(
				new URI(CliConfSingleton.publicAddr + "/api/app/" + userID
						+ "/content/" + content.getContentsID())).build();

	}

	/**
	 * post a file on the box for the userID
	 * 
	 * @param userID
	 *            the sender of the request
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param body
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@POST
	@RolesAllowed({ "other", "authenticated" })
	@Consumes(MediaType.WILDCARD)
	public Response postContent2(@PathParam("userID") String userID,
			InputStream uploadedInputStream,
			@HeaderParam("Content-Disposition") String contentDisposition)
			throws URISyntaxException, IOException {

		LOGGER.debug("New local upload, Content-Disposition : "
				+ contentDisposition);
		try {
			Content content = uManager.createContent(userID,
					uploadedInputStream, contentDisposition);
			content.setLink(CliConfSingleton.publicAddr + content.getLink());

			LOGGER.debug("Content created :" + CliConfSingleton.publicAddr
					+ "/api/app/" + userID + "/content/"
					+ content.getContentsID());
			return Response.created(
					new URI(CliConfSingleton.publicAddr + "/api/app/" + userID
							+ "/content/" + content.getContentsID())).build();
		} catch (IOException | SecurityException e) {
			throw e;
		}
	}

	// @POST
	// @Consumes(MediaType.APPLICATION_OCTET_STREAM)
	// @Produces(MediaType.TEXT_PLAIN)
	// public String postIf(InputStream is) throws IOException{
	// java.nio.file.Path path = java.nio.file.Files.createTempFile(null, null);
	// FileOutputStream fos = new FileOutputStream(path.toFile());
	// ByteStreams.copy(is, fos);
	// fos.close();
	// is.close();
	//
	// TikaConfig config = TikaConfig.getDefaultConfig();
	// Detector detector = config.getDetector();
	//
	// TikaInputStream stream = TikaInputStream.get(path.toFile());
	//
	// Metadata metadata = new Metadata();
	// metadata.add(Metadata.RESOURCE_NAME_KEY, path.toString());
	// org.apache.tika.mime.MediaType mediaType = detector.detect(stream,
	// metadata);
	//
	// return mediaType.toString() + "   --   " + path.toString();
	//
	// }

	// @GET
	// @Path("get")
	// @RolesAllowed({ "other", "authenticated" })
	// public Response getTest() {
	// // LOGGER.error("Is only from local not from {}", request);
	//
	// return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	// }

	/**
	 * Update information for the video
	 * 
	 * @param content
	 *            the content
	 * @param contentsID
	 *            the id of the content
	 * @return
	 */
	@PUT
	@RolesAllowed({ "authenticated", "other" })
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putContent(Content content,
			@PathParam("contentsID") String contentsID) {
		// TODO: need to check the authentication of the user
		content.setContentsID(contentsID);
		// modify the content
		if (uManager.contentExist(content.getContentsID()) == true) {
			uManager.saveContent(content);
			return Response.status(200).build();
		} else {
			return Response.status(409).build();
		}
	}

	/**
	 * delete the contents with contentsID
	 * 
	 * @param contentsID
	 *            the contentsID to delete
	 * @return
	 */
	@DELETE
	@RolesAllowed("other")
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteContent(@PathParam("contentsID") String contentsID) {
		// TODO: need to check the authentication of the user

		// delete the content
		uManager.deleteContent(contentsID);
		return Response.status(200).build();

	}

}
