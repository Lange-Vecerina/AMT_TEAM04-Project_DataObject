package org.heig.team04.dataobject.service;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.lang.annotation.Inherited;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;

/**
 * This class is the implementation of the service layer for the AWS S3 service.
 * It is used to perform CRUD operations on the S3 service and to publish a link to a resource.
 *
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 *
 * @see ServiceInterface
 * @see S3Client
 */
public class AppServiceAWS implements ServiceInterface {
    private final S3Client s3; // AWS S3 client
    private final S3Presigner presigner; // AWS S3 presigner

    /**
     * Constructor for the AppServiceAWS class.
     * It creates a new S3Client object with the credentials provided by the environment variables.
     */
    public AppServiceAWS() {
        s3 = S3Client.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        presigner = S3Presigner.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    /**
     * Checks if the given bucketName is a bucket in the S3 service.
     * @param bucketName the name of the bucket to check
     * @param key the path to the folder to check
     * @return true if the bucket exists, false otherwise
     */
    private boolean isBucket(String bucketName, String key) {
        if (key == null) {
            return s3.listBuckets().buckets().stream().anyMatch(b -> b.name().equals(bucketName));
        }

        return false;
    }

    /**
     * Checks if the given folderName is a folder in the S3 service.
     * @param bucketName the name of the bucket to check
     * @param key the path to the folder to check
     * @return true if path is a folder, false otherwise
     */
    private boolean isFolder(String bucketName, String key) {
        return !s3.listObjectsV2(builder -> builder.bucket(bucketName).prefix(key).delimiter("/")).commonPrefixes().isEmpty();
    }

    /**
     * Checks if the given fileName is a file in the S3 service.
     * @param bucketName the name of the bucket to check
     * @param key the path to the file to check
     * @return true if path is a file, false otherwise
     */
    private boolean isFile(String bucketName, String key) {
        return s3.listObjectsV2(builder -> builder.bucket(bucketName).prefix(key).delimiter("/")).contents().size() > 0;
    }

    /**
     * Split a resource URI into bucket name and key (folder or file)
     * @param uri Resource URI
     * @return String array with bucket name at index 0 and key at index 1
     */
    private String[] splitUri(String uri) {
        // Remove Trailing slash
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        // Split URI into bucket and key
        String[] parts = uri.split("/", 2);
        String bucketName = parts[0];
        String key = parts.length > 1 ? parts[1] : null;

        return new String[]{bucketName, key};
    }

    @Override
    public boolean create(String resourceUri, String fileUrl) {
        byte[] fileContent = null;

        try {
            URLConnection connection = new URL(fileUrl).openConnection();
            fileContent = connection.getInputStream().readAllBytes();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL");
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not read file from URL");
        }

        return create(resourceUri, fileContent);
    }

    @Override
    public boolean create(String resourceUri, byte[] fileContent) {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to an existing object, throw an exception
        if (exists(resourceUri)) {
            throw new IllegalArgumentException("The resource URI already exists");
        }

        // If bucket does not exist, create it
        if (!isBucket(bucketName, null)) {
            s3.createBucket(builder -> builder.bucket(bucketName));
        }

        // Prepare the request
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Create the object
        s3.putObject(request, RequestBody.fromBytes(fileContent));

        return exists(resourceUri);
    }

    @Override
    public byte[] read(String resourceUri) {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to a non-existing object, throw an exception
        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("The resource URI does not exist");
        }

        // If the URI points to a folder or bucket, throw an exception
        if (isFolder(bucketName, key) || isBucket(bucketName, key)) {
            throw new IllegalArgumentException("The resource URI points to a folder or bucket");
        }

        // Prepare the request
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Read the object
        return s3.getObjectAsBytes(request).asByteArray();
    }

    @Override
    public boolean update(String resourceUri, String fileUrl) {
        byte[] fileContent = null;

        try {
            URLConnection connection = new URL(fileUrl).openConnection();
            fileContent = connection.getInputStream().readAllBytes();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL");
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not read file from URL");
        }

        return update(resourceUri, fileContent);
    }

    @Override
    public boolean update(String resourceUri, byte[] fileContent) {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to a non-existing object, throw an exception
        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("The resource URI does not exist");
        }

        // If the URI points to a folder or bucket, throw an exception
        if (isFolder(bucketName, key) || isBucket(bucketName, key)) {
            throw new IllegalArgumentException("The resource URI points to a folder or bucket");
        }

        // Prepare the request
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Update the object
        s3.putObject(request, RequestBody.fromBytes(fileContent));

        return exists(resourceUri);
    }

    @Override
    public boolean delete(String resourceUri, boolean recursive) {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to a non-existing object, throw an exception
        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("The resource URI does not exist");
        }

        // If the URI points to a folder or bucket and recursive is false, throw an exception
        if (!recursive && (isFolder(bucketName, key) || isBucket(bucketName, key))) {
            throw new IllegalArgumentException("The resource URI points to a folder or bucket and recursive is false");
        }

        // If the URI points to a bucket, delete all objects in the bucket recursively and then delete the bucket
        if (isBucket(bucketName, key)) {
            // Delete all objects in the bucket recursively
            ListObjectsV2Response response = s3.listObjectsV2(builder -> builder.bucket(bucketName));
            for (S3Object object : response.contents()) {
                s3.deleteObject(builder -> builder.bucket(bucketName).key(object.key()));
            }

            // Delete the bucket
            s3.deleteBucket(builder -> builder.bucket(bucketName));

            return !exists(resourceUri);
        }

        // If the URI points to a folder, delete all objects in the folder recursively
        if (isFolder(bucketName, key)) {
            // Delete all objects in the folder recursively
            ListObjectsV2Response response = s3.listObjectsV2(builder -> builder.bucket(bucketName).prefix(key));
            for (S3Object object : response.contents()) {
                s3.deleteObject(builder -> builder.bucket(bucketName).key(object.key()));
            }

            return !exists(resourceUri);
        }

        // If the URI points to an object, delete the object
        s3.deleteObject(builder -> builder.bucket(bucketName).key(key));

        return !exists(resourceUri);
    }

    @Override
    public String publish(String resourceUri, int ttl) {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to a non-existing object, throw an exception
        if (!exists(resourceUri)) {
            throw new IllegalArgumentException("The resource URI does not exist");
        }

        // If the URI points to a folder or bucket, throw an exception
        if (isFolder(bucketName, key) || isBucket(bucketName, key)) {
            throw new IllegalArgumentException("The resource URI points to a folder or bucket");
        }

        // Prepare presigned URL GET request
        var request = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(ttl))
                .getObjectRequest(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build())
                .build();

        // Generate presigned URL
        return presigner.presignGetObject(request).toString();
    }

    @Override
    public boolean exists(String resourceUri) {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        if (!isBucket(bucketName, null)) {
            return false;
        }

        if (isBucket(bucketName, key)) {
            return true;
        }

        if (isFolder(bucketName, key)) {
            return true;
        }

        return isFile(bucketName, key);
    }
}
