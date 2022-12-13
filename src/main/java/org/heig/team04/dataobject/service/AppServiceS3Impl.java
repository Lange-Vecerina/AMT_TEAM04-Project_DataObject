package org.heig.team04.dataobject.service;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.stereotype.Service;

@Service
public class AppServiceS3Impl implements org.heig.team04.dataobject.service.ServiceInterface {
    private final AmazonS3 amazonS3;
    private static final int TTL = 1800;

    public AppServiceS3Impl() {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.EU_WEST_2)
                .build();
    }

    private String[] splitUri(String resourceUri) {
        return resourceUri.split("/", 2);
    }

    private

    @Override
    public void create(String resourceUri) {
        appRepository.create(resourceUri);
    }

    @Override
    public void create(String resourceUri, String fileUrl) {
        appRepository.create(resourceUri, fileUrl);
    }

    @Override
    public void create(String resourceUri, byte[] fileContent) {
        appRepository.create(resourceUri, fileContent);
    }

    @Override
    public byte[] read(String resourceUri) {
        return appRepository.read(resourceUri);
    }

    @Override
    public void update(String resourceUri, String fileUrl) {
        appRepository.update(resourceUri, fileUrl);
    }

    @Override
    public void update(String resourceUri, byte[] fileContent) {
        appRepository.update(resourceUri, fileContent);
    }

    @Override
    public void delete(String resourceUri) {
        appRepository.delete(resourceUri);
    }

    @Override
    public String publish(String resourceUri) {
        String[] splitUri = splitUri(resourceUri);

        if (exists(resourceUri)) {
            return amazonS3.generatePresignedUrl(
                    splitUri[0],
                    splitUri[1],
                    java.util.Date.from(
                            java.time.Instant
                                    .now()
                                    .plusSeconds(TTL)))
                    .toString();
        }

        return null;
    }

    @Override
    public boolean exists(String resourceUri) {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri[1].length() == 0) {
            return amazonS3.doesBucketExistV2(splitUri[0]);
        }

        return amazonS3.doesObjectExist(splitUri[0], splitUri[1]);
    }
}
