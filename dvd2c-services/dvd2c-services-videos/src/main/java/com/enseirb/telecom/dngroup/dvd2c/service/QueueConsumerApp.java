package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;

public interface QueueConsumerApp {

	/**
	 * Create a queue with the correlation_id of the task, as we can wait the end message
	 * @param queue
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public abstract void getQueueMessage(String queue,
			ContentService contentService) throws IOException,
			InterruptedException;

}