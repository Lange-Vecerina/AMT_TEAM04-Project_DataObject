package org.heig.team04.dataobject.controller;

import org.heig.team04.dataobject.service.AppService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data-object")
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/create")
    public void create(@RequestParam String resourceUri) {
        appService.create(resourceUri);
    }

    @PostMapping("/create")
    public void create(@RequestParam String resourceUri, @RequestParam String fileUrl) {
        appService.create(resourceUri, fileUrl);
    }

    @PostMapping("/create")
    public void create(@RequestParam String resourceUri, @RequestBody byte[] fileContent) {
        appService.create(resourceUri, fileContent);
    }

    @GetMapping("/read")
    public byte[] read(@RequestParam String resourceUri) {
        return appService.read(resourceUri);
    }

    @PutMapping("/update")
    public void update(@RequestParam String resourceUri, @RequestParam String fileUrl) {
        appService.update(resourceUri, fileUrl);
    }

    @PutMapping("/update")
    public void update(@RequestParam String resourceUri, @RequestBody byte[] fileContent) {
        appService.update(resourceUri, fileContent);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String resourceUri) {
        appService.delete(resourceUri);
    }

    @GetMapping("/publish")
    public String publish(@RequestParam String resourceUri) {
        return appService.publish(resourceUri);
    }

    @GetMapping("/exists")
    public boolean exists(@RequestParam String resourceUri) {
        return appService.exists(resourceUri);
    }
}
