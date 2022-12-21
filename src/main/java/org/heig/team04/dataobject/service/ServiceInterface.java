package org.heig.team04.dataobject.service;

import org.springframework.stereotype.Service;
import org.heig.team04.dataobject.service.exceptions.*;

/**
 * Service interface for the data object service.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
@Service
public interface ServiceInterface {
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
    boolean create(String resourceUri, String fileUrl) throws InvalidURLException, URLNotAccessibleException, AlreadyExistsException, ExternalServiceException;

    /**
     * Create a new object at the given URI with the given content.
     * @param resourceUri The URI of the new object.
     * @param fileContent The content of the new object.
     * @return boolean True if the object was created, false otherwise.
     * @throws AlreadyExistsException If the URI already exists.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    boolean create(String resourceUri, byte[] fileContent) throws AlreadyExistsException, ExternalServiceException;

    /**
     * Read the content of the object at the given URI.
     * @param resourceUri The URI of the object.
     * @return byte[] The content of the object.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws NotAnObjectException If the URI points to a collection.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    byte[] read(String resourceUri) throws NotFoundException, NotAnObjectException, ExternalServiceException;

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
    boolean update(String resourceUri, String fileUrl) throws NotFoundException, NotAnObjectException, InvalidURLException, URLNotAccessibleException, ExternalServiceException;

    /**
     * Update the content of the object at the given URI with the given content.
     * @param resourceUri The URI of the object.
     * @param fileContent The content of the object.
     * @return boolean True if the object was updated, false otherwise.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws NotAnObjectException If the URI points to a collection.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    boolean update(String resourceUri, byte[] fileContent) throws NotFoundException, NotAnObjectException, ExternalServiceException;

    /**
     * Delete the object at the given URI.
     * @param resourceUri The URI of the object.
     * @return boolean True if the object was deleted, false otherwise.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws DeleteCollectionNoRecursiveException If the URI points to a collection and the recursive option is not set to true.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    boolean delete(String resourceUri, boolean recursive) throws NotFoundException, DeleteCollectionNoRecursiveException, ExternalServiceException;

    /**
     * Get a presigned URL to access the object at the given URI.
     * @param resourceUri The URI of the object.
     * @param ttl The time to live of the URL in seconds.
     * @return String The presigned URL.
     * @throws NotFoundException If the URI doesn't exist.
     * @throws NotAnObjectException If the URI points to a collection.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    String publish(String resourceUri, int ttl) throws NotFoundException, NotAnObjectException, ExternalServiceException;

    /**
     * Check if the object at the given URI exists.
     * @param resourceUri The URI of the object.
     * @return boolean True if the object exists, false otherwise.
     * @throws ExternalServiceException If an internal error occurs with the S3 service.
     */
    boolean exists(String resourceUri) throws ExternalServiceException;

}
