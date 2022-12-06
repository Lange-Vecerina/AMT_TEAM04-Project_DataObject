package org.heig.team04.dataobject.service;

public interface ServiceInterface {
    void create(String ressourceUri);

    void create(String ressourceUri, String fileUrl);

    void create(String ressourceUri, byte[] fileContent);

    byte[] read(String ressourceUri);

    void update(String ressourceUri, String fileUrl);

    void update(String ressourceUri, byte[] fileContent);

    void delete(String ressourceUri);

    String publish(String ressourceUri);

    boolean exists(String ressourceUri);
}
