package com.github.kdlug;

import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * The type Google cloud storage.
 */
public class GoogleCloudStorage {
    Storage storage;
    Bucket bucket;

    /**
     * Instantiates a client
     * GOOGLE_APPLICATION_CREDENTIALS environment variable should be set
     */
    public GoogleCloudStorage(String bucketName) {
        storage = StorageOptions.getDefaultInstance().getService();
        bucket = getBucketByName(bucketName);
    }

    /**
     * Instantiates a client
     *
     * @param configPath file path of the JSON file that contains your service account key
     * @param projectId google cloud project ID
     * @param bucketName name of the bucket
     * @throws IOException
     */
    public GoogleCloudStorage(String configPath, String projectId, String bucketName) throws IOException {
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(configPath));

        storage = StorageOptions.newBuilder().setCredentials(credentials)
				.setProjectId(projectId).build().getService();

        bucket = getBucketByName(bucketName);
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public BlobId createObject(String blobName, String content) {
        byte[] bytes = content.getBytes(UTF_8);
        Blob blob = bucket.create(blobName, bytes);

        return blob.getBlobId();
    }

    public String readBlobByName(String name) throws Exception {
        BlobId blobId = getBlobIdByName(name);

        if (blobId == null) throw new Exception("Could not find object " + name + " in" + bucket);

        Blob blob = storage.get(blobId);

        return new String(blob.getContent());
    }

    public String readBlob(BlobId blobId) {
        Blob blob = storage.get(blobId);

        return new String(blob.getContent());
    }

    public void updateBlob(BlobId blobId, String content) throws IOException {
        Blob blob = storage.get(blobId);
        WritableByteChannel channel = blob.writer();
        channel.write(ByteBuffer.wrap(content.getBytes(UTF_8)));
        channel.close();
    }

    public boolean deleteBlob(BlobId blobId) {
        return storage.delete(blobId);
    }

    public BlobId getBlobIdByName(String blobName) {
        return BlobId.of(bucket.getName(), blobName);
    }

    public Blob getBlob(BlobId blobId) {
        return storage.get(blobId);
    }

    public boolean isBlobExists(String blobName) {
        BlobId blobId = getBlobIdByName(blobName);

        return getBlob(blobId).exists();
    }

    private Bucket getBucketByName(String bucketName) {
        bucket = storage.get(bucketName);

        if (bucket == null) {
            bucket = storage.create(BucketInfo.of(bucketName));
        }

        return bucket;
    }
}
