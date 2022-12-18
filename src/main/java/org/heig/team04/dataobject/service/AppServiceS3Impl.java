package org.heig.team04.dataobject.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

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

    private void putByteArrayOnS3(byte[] fileContent, String[] splitUri) {
        Path tmpFilePath = null;
        try {
            tmpFilePath = Files.createTempFile("tmp", ".tmp");
            Files.write(tmpFilePath, fileContent);

            amazonS3.putObject(splitUri[0], splitUri[1], tmpFilePath.toFile());

            Files.delete(tmpFilePath);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error creating object", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid resourceUri", e);
        } catch (IOException e) {
            throw new AmazonServiceException("Internal error", e);
        }
    }

    private void putSourceContentOnS3(String fileUrl, String[] splitUri) throws AmazonServiceException,IllegalArgumentException {
        try {
            URL url = new URL(fileUrl);
            InputStream is = url.openStream();
            String suffix = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
            File tempFile = File.createTempFile("tempImg", "." + suffix);
            OutputStream os = Files.newOutputStream(tempFile.toPath());

            IOUtils.copy(is, os);

            amazonS3.putObject(splitUri[0], splitUri[1], tempFile);

            is.close();
            os.close();

            if (!tempFile.delete()) {
                throw new IOException("Error deleting temp file");
            }
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error creating object", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid resourceUri", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Internal error", e);
        }
    }

    @Override
    public void create(String resourceUri) throws IllegalArgumentException, AmazonServiceException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length > 2 || splitUri[1].length() != 0) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (exists(splitUri[0])) {
            throw new IllegalArgumentException("Resource already exists");
        }

        try {
            amazonS3.createBucket(splitUri[0]);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error creating bucket", e);
        }
    }

    @Override
    public void create(String resourceUri, String fileUrl) throws IllegalArgumentException, AmazonServiceException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length != 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (exists(resourceUri)) {
            throw new IllegalArgumentException("Resource already exists");
        }

        try {
            if (!exists(splitUri[0])) {
                create(splitUri[0]);
            }

            putSourceContentOnS3(fileUrl, splitUri);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error creating object", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid resourceUri", e);
        }

    }

    @Override
    public void create(String resourceUri, byte[] fileContent) throws IllegalArgumentException, AmazonServiceException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length != 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (exists(resourceUri)) {
            throw new IllegalArgumentException("Resource already exists");
        }


        try {
            if (!exists(splitUri[0])) {
                create(splitUri[0]);
            }

            putByteArrayOnS3(fileContent, splitUri);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error creating object", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid resourceUri", e);
        }
    }

    @Override
    public byte[] read(String resourceUri) throws IllegalArgumentException, AmazonServiceException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length != 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("Resource doesn't exist");
        }

        try {
            return amazonS3.getObject(splitUri[0], splitUri[1]).getObjectContent().readAllBytes();
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error reading object", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Internal error", e);
        }
    }

    @Override
    public void update(String resourceUri, String fileUrl) throws IllegalArgumentException, AmazonServiceException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length != 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("Resource doesn't exist");
        }

        try {
            putSourceContentOnS3(fileUrl, splitUri);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error updating object", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid resourceUri", e);
        }
    }

    @Override
    public void update(String resourceUri, byte[] fileContent) throws IllegalArgumentException, AmazonServiceException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length != 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("Resource doesn't exist");
        }

        try {
            putByteArrayOnS3(fileContent, splitUri);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error updating object", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid resourceUri", e);
        }
}

    @Override
    public void delete(String resourceUri) {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length != 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("Resource doesn't exist");
        }

        try {
            amazonS3.deleteObject(splitUri[0], splitUri[1]);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("Error deleting object", e);
        }
    }

    @Override
    public String publish(String resourceUri) throws IllegalArgumentException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length != 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("Resource doesn't exist");
        }

        try {
            return amazonS3.generatePresignedUrl(splitUri[0], splitUri[1], java.util.Date.from(java.time.Instant.now().plusSeconds(TTL))).toString();
        } catch (SdkClientException e) {
            throw new IllegalArgumentException("Error publishing object", e);
        }
    }

    @Override
    public boolean exists(String resourceUri) throws AmazonServiceException {
        String[] splitUri = splitUri(resourceUri);

        if (splitUri.length > 2) {
            throw new IllegalArgumentException("Invalid resourceUri");
        }

        if (splitUri.length == 2) {
            try {
                return amazonS3.doesObjectExist(splitUri[0], splitUri[1]);
            } catch (AmazonServiceException e) {
                throw new AmazonServiceException("Error checking object existence", e);
            }
        } else {
            try {
                return amazonS3.doesBucketExistV2(splitUri[0]);
            } catch (AmazonServiceException e) {
                throw new AmazonServiceException("Error checking object existence", e);
            }
        }
    }
}
