package org.heig.team04.dataobject.service;

import org.heig.team04.dataobject.service.exceptions.*;
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
public class ServiceAwsImpl implements ServiceInterface {
    private final S3Client s3; // AWS S3 client
    private final S3Presigner presigner; // AWS S3 presigner

    /**
     * Constructor for the AppServiceAWS class.
     * It creates a new S3Client object with the credentials provided by the environment variables.
     */
    public ServiceAwsImpl() {
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
     * @throws ExternalServiceException if an error occurs while checking if the bucket exists
     */
    private boolean isBucket(String bucketName, String key) throws ExternalServiceException {
        try {
            if (key == null) {
                return s3.listBuckets().buckets().stream().anyMatch(b -> b.name().equals(bucketName));
            }

            return false;
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }
    }

    /**
     * Checks if the given folderName is a folder in the S3 service.
     * @param bucketName the name of the bucket to check
     * @param key the path to the folder to check
     * @return true if path is a folder, false otherwise
     * @throws ExternalServiceException if an error occurs while checking if the folder exists
     */
    private boolean isFolder(String bucketName, String key) throws ExternalServiceException {
        try {
            return !s3.listObjectsV2(builder -> builder.bucket(bucketName).prefix(key).delimiter("/")).commonPrefixes().isEmpty();
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }
    }

    /**
     * Checks if the given fileName is a file in the S3 service.
     * @param bucketName the name of the bucket to check
     * @param key the path to the file to check
     * @return true if path is a file, false otherwise
     * @throws ExternalServiceException if an error occurs while checking if the file exists
     */
    private boolean isFile(String bucketName, String key) throws ExternalServiceException {
        try {
            return !s3.listObjectsV2(builder -> builder.bucket(bucketName).prefix(key)).contents().isEmpty();
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }
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

    /**
     * Checks if the given resource URI is a valid resource in the S3 service.
     * @param resourceUri the URI of the resource to check
     * @param bucketName the name of the bucket to check
     * @param key the path to the resource to check
     * @throws ExternalServiceException if the resource does not exist
     * @throws NotFoundException if the resource does not exist
     * @throws NotAnObjectException if the resource is not an object
     */
    private void checkForGET(String resourceUri, String bucketName, String key) throws ExternalServiceException, NotFoundException, NotAnObjectException {
        // If the URI points to a non-existing object, throw an exception
        if (!exists(resourceUri)) {
            throw new NotFoundException(resourceUri);
        }

        // If the URI points to a folder or bucket, throw an exception
        if (isFolder(bucketName, key) || isBucket(bucketName, key)) {
            throw new NotAnObjectException(resourceUri);
        }
    }

    /**
     * Get the content of a file at the given URL
     * @param fileUrl URL of the file
     * @return Content of the file
     * @throws InvalidURLException if the URL is invalid
     * @throws URLNotAccessibleException if an error occurs while getting the file content
     */
    private byte[] getContentFromURL(String fileUrl) throws InvalidURLException, URLNotAccessibleException {
        byte[] fileContent;

        try {
            URLConnection connection = new URL(fileUrl).openConnection();
            fileContent = connection.getInputStream().readAllBytes();
        } catch (MalformedURLException e) {
            throw new InvalidURLException(fileUrl, e);
        } catch (Exception e) {
            throw new URLNotAccessibleException(fileUrl, e);
        }

        return fileContent;
    }

    /**
     * Create a new object at the given URI with the content of the given source.
     * @param resourceUri The URI of the new object.
     * @param fileUrl The URL of the source file.
     * @return boolean True if the object was created, false otherwise.
     * @throws AlreadyExistsException If the URI already exists.
     * @throws InvalidURLException If the URL is not valid.
     * @throws URLNotAccessibleException If the URL is not accessible.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public boolean create(String resourceUri, String fileUrl) throws AlreadyExistsException, InvalidURLException, URLNotAccessibleException, ExternalServiceException {
        byte[] fileContent = getContentFromURL(fileUrl);

        return create(resourceUri, fileContent);
    }

    /**
     * Create a new object at the given URI with the given content.
     * @param resourceUri The URI of the new object.
     * @param fileContent The content of the new object.
     * @return boolean True if the object was created, false otherwise.
     * @throws AlreadyExistsException If the URI already exists.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public boolean create(String resourceUri, byte[] fileContent) throws AlreadyExistsException, ExternalServiceException {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to an existing object, throw an exception
        if (exists(resourceUri)) {
            throw new AlreadyExistsException(resourceUri);
        }

        // If bucket does not exist, create it
        if (!isBucket(bucketName, null)) {
            s3.createBucket(builder -> builder.bucket(bucketName));
        }

        try {
            // Prepare the request
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Create the object
            s3.putObject(request, RequestBody.fromBytes(fileContent));
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }

        return exists(resourceUri);
    }

    /**
     * Read the content of the object at the given URI.
     * @param resourceUri The URI of the object.
     * @return byte[] The content of the object.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws NotAnObjectException If the URI points to a collection.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public byte[] read(String resourceUri) throws NotFoundException, NotAnObjectException, ExternalServiceException {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        checkForGET(resourceUri, bucketName, key);

        try {
            // Prepare the request
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Read the object
            return s3.getObjectAsBytes(request).asByteArray();
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }
    }

    /**
     * Update the content of the object at the given URI with the content of the given source.
     * @param resourceUri The URI of the object.
     * @param fileUrl The URL of the source file.
     * @return boolean True if the object was updated, false otherwise.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws NotAnObjectException If the URI points to a collection.
     * @throws InvalidURLException If the URL is not valid.
     * @throws URLNotAccessibleException If the URL is not accessible.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public boolean update(String resourceUri, String fileUrl) throws NotFoundException, NotAnObjectException, InvalidURLException, URLNotAccessibleException, ExternalServiceException {
        byte[] fileContent = getContentFromURL(fileUrl);

        return update(resourceUri, fileContent);
    }

    /**
     * Update the content of the object at the given URI with the given content.
     * @param resourceUri The URI of the object.
     * @param fileContent The content of the object.
     * @return boolean True if the object was updated, false otherwise.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws NotAnObjectException If the URI points to a collection.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public boolean update(String resourceUri, byte[] fileContent) throws NotFoundException, NotAnObjectException, ExternalServiceException {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to a non-existing object, throw an exception
        checkForGET(resourceUri, bucketName, key);

        try {
            // Prepare the request
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Update the object
            s3.putObject(request, RequestBody.fromBytes(fileContent));
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }

        return exists(resourceUri);
    }

    /**
     * Delete the object at the given URI.
     * @param resourceUri The URI of the object.
     * @return boolean True if the object was deleted, false otherwise.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws DeleteCollectionNoRecursiveException If the URI points to a collection and the recursive option is not set to true.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public boolean delete(String resourceUri, boolean recursive) throws NotFoundException, DeleteCollectionNoRecursiveException, ExternalServiceException {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to a non-existing object, throw an exception
        if (!exists(resourceUri)) {
            throw new NotFoundException(resourceUri);
        }

        // If the URI points to a folder or bucket and recursive is false, throw an exception
        if (!recursive && (isFolder(bucketName, key) || isBucket(bucketName, key))) {
            throw new DeleteCollectionNoRecursiveException(resourceUri);
        }

        try {
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
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }

        return !exists(resourceUri);
    }

    /**
     * Get a presigned URL to access the object at the given URI.
     * @param resourceUri The URI of the object.
     * @param ttl The time to live of the URL in seconds.
     * @return String The presigned URL.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws NotAnObjectException If the URI points to a collection.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public String publish(String resourceUri, int ttl) throws NotFoundException, NotAnObjectException, ExternalServiceException {
        String[] parts = splitUri(resourceUri);
        String bucketName = parts[0];
        String key = parts[1];

        // If the URI points to a non-existing object, throw an exception
        checkForGET(resourceUri, bucketName, key);

        try {
            // Prepare presigned URL GET request
            var request = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(ttl))
                    .getObjectRequest(b -> b.bucket(bucketName).key(key))
                    .build();

            // Generate presigned URL
            return presigner.presignGetObject(request).toString();
        } catch (Exception e) {
            throw new ExternalServiceException(e);
        }
    }

    /**
     * Check if the object at the given URI exists.
     * @param resourceUri The URI of the object.
     * @return boolean True if the object exists, false otherwise.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    @Override
    public boolean exists(String resourceUri) throws ExternalServiceException {
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
