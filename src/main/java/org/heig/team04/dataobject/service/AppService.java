package org.heig.team04.dataobject.service;

import org.heig.team04.dataobject.repository.AppRepository;
import org.springframework.stereotype.Service;

@Service
public class AppService implements org.heig.team04.dataobject.service.ServiceInterface {
    private final AppRepository appRepository;

    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

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
        return appRepository.publish(resourceUri);
    }

    @Override
    public boolean exists(String resourceUri) {
        return appRepository.exists(resourceUri);
    }
}
