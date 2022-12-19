package org.heig.team04.dataobject.service;

public interface ServiceInterface {
    void create(String resourceUri, String fileUrl);

    void create(String resourceUri, byte[] fileContent);

    byte[] read(String resourceUri);

    void update(String resourceUri, String fileUrl);

    void update(String resourceUri, byte[] fileContent);

    void delete(String resourceUri, boolean recursive);

    String publish(String resourceUri, int ttl);

    boolean exists(String resourceUri);
}
