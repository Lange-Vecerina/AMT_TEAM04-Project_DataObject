package org.heig.team04.dataobject.controller;

import org.heig.team04.dataobject.service.AppServiceS3Impl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data-object")
public class AppController {

    private final AppServiceS3Impl appServiceS3Impl;

    public AppController(AppServiceS3Impl appServiceS3Impl) {
        this.appServiceS3Impl = appServiceS3Impl;
    }

    @PostMapping("/create")
    public void create(@RequestParam String resourceUri) {
        appServiceS3Impl.create(resourceUri);
    }

    @PostMapping("/create")
    public void create(@RequestParam String resourceUri, @RequestParam String fileUrl) {
        appServiceS3Impl.create(resourceUri, fileUrl);
    }

    @PostMapping("/create")
    public void create(@RequestParam String resourceUri, @RequestBody byte[] fileContent) {
        appServiceS3Impl.create(resourceUri, fileContent);
    }

    @GetMapping("/read")
    public byte[] read(@RequestParam String resourceUri) {
        return appServiceS3Impl.read(resourceUri);
    }

    @PutMapping("/update")
    public void update(@RequestParam String resourceUri, @RequestParam String fileUrl) {
        appServiceS3Impl.update(resourceUri, fileUrl);
    }

    @PutMapping("/update")
    public void update(@RequestParam String resourceUri, @RequestBody byte[] fileContent) {
        appServiceS3Impl.update(resourceUri, fileContent);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String resourceUri) {
        appServiceS3Impl.delete(resourceUri);
    }

    @GetMapping("/publish")
    public String publish(@RequestParam String resourceUri) {
        return appServiceS3Impl.publish(resourceUri);
    }

    @GetMapping("/exists")
    public boolean exists(@RequestParam String resourceUri) {
        return appServiceS3Impl.exists(resourceUri);
    }
}
