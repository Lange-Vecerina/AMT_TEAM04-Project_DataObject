package org.heig.team04.dataobject.repository;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class AppRepository {
    private final AmazonS3 amazonS3;

    public AppRepository() {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.EU_WEST_2)
                .build();
    }

    public void create(String resourceUri) {
        amazonS3.createBucket(resourceUri);
    }

    public void create(String resourceUri, String fileUrl) {
        // TODO: Create object in S3
        // Download file from fileUrl and convert it to byte[]
        // byte[] fileContent = ...;
        // create(resourceUri, fileContent);
    }

    public void create(String resourceUri, byte[] fileContent) {
        // TODO: Create object in S3
        // amazonS3.putObject(resourceUri, fileContent);
    }

    public byte[] read(String resourceUri) {
        // TODO: Read object in S3
        // S3Object s3Object = amazonS3.getObject(resourceUri);
        // InputStream inputStream = s3Object.getObjectContent();
        // return IOUtils.toByteArray(inputStream);

        return null;
    }

    public void update(String resourceUri, String fileUrl) {
        // TODO: Update object in S3
        // Download file from fileUrl and convert it to byte[]
        // byte[] fileContent = ...;
        // update(resourceUri, fileContent);
    }

    public void update(String resourceUri, byte[] fileContent) {
        // TODO: Update object in S3
        // amazonS3.putObject(resourceUri, fileContent);
    }

    public void delete(String resourceUri) {
        // TODO: Delete object in S3
        // amazonS3.deleteObject(resourceUri);
    }

    public String publish(String resourceUri) {
        // TODO: Get public URL of the object
        // Get public URL of the object
        // String publicUrl = amazonS3.getUrl(resourceUri).toString();
        // return publicUrl;

        return null;
    }

    public boolean exists(String resourceUri) {
        // TODO: Check if object exists in S3
        // return amazonS3.doesObjectExist(resourceUri);

        return false;
    }
}
