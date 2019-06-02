package com.github.kdlug;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String bucketName = "bucket-name";
		String blobName = "test-object";

		BlobId blobId;

		GoogleCloudStorage storage = new GoogleCloudStorage(bucketName);

		log.info("Checking if blob {} exists", blobName);

		if (storage.isBlobExists(blobName)) {
			log.info("Updating object {}", blobName);
			blobId = storage.getBlobIdByName(blobName);
			storage.updateBlob(blobId, new Date().toString());
		} else {
			log.info("Creating object {}", blobName);
			blobId = storage.createObject(blobName, "some new content");
		}

		blobId = storage.getBlobIdByName(blobName);
		Blob blob = storage.getBlob(blobId);

		log.info("Reading object #{}", blobId.toString());
		String content = storage.readBlob(blobId);
		log.info(content);

		log.info("Reading object {}", blobName);
		content = storage.readBlobByName(blobName);
		log.info(content);
	}
}
