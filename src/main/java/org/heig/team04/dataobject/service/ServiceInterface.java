package org.heig.team04.dataobject.service;

import org.springframework.stereotype.Service;

@Service
public interface ServiceInterface {
    /**
     * Create a new object at the given URI with the content of the given source.
     * @param resourceUri The URI of the new object.
     * @param fileUrl The URL of the source file.
     * @return boolean True if the object was created, false otherwise.
     */
    boolean create(String resourceUri, String fileUrl);

    /**
     * Create a new object at the given URI with the given content.
     * @param resourceUri The URI of the new object.
     * @param fileContent The content of the new object.
     * @return boolean True if the object was created, false otherwise.
     */
    boolean create(String resourceUri, byte[] fileContent);

    /**
     * Read the content of the object at the given URI.
     * @param resourceUri The URI of the object.
     * @return byte[] The content of the object.
     */
    byte[] read(String resourceUri);

    /**
     * Update the content of the object at the given URI with the content of the given source.
     * @param resourceUri The URI of the object.
     * @param fileUrl The URL of the source file.
     * @return boolean True if the object was updated, false otherwise.
     */
    boolean update(String resourceUri, String fileUrl);

    /**
     * Update the content of the object at the given URI with the given content.
     * @param resourceUri The URI of the object.
     * @param fileContent The content of the object.
     * @return boolean True if the object was updated, false otherwise.
     */
    boolean update(String resourceUri, byte[] fileContent);

    /**
     * Delete the object at the given URI.
     * @param resourceUri The URI of the object.
     * @return boolean True if the object was deleted, false otherwise.
     */
    boolean delete(String resourceUri, boolean recursive);

    /**
     * Get a presigned URL to access the object at the given URI.
     * @param resourceUri The URI of the object.
     * @param ttl The time to live of the URL in seconds.
     * @return String The presigned URL.
     */
    String publish(String resourceUri, int ttl);

    /**
     * Check if the object at the given URI exists.
     * @param resourceUri The URI of the object.
     * @return boolean True if the object exists, false otherwise.
     */
    boolean exists(String resourceUri);
}
