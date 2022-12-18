package org.heig.team04.dataobject.controller;

import org.heig.team04.dataobject.service.ServiceInterface;
import org.springframework.web.bind.annotation.*;
import org.heig.team04.dataobject.dto.DTOs;

@RestController
@RequestMapping("/data-object")
public class AppController {

    private final ServiceInterface appService;

    public AppController(ServiceInterface appService) {
        this.appService = appService;
    }

    @PostMapping("/create")
    public void create(@RequestBody DTOs.ObjectDTO objectDTO) {
        appService.create(objectDTO.getUri());
    }

    @PostMapping("/create")
    public void create(@RequestBody DTOs.ObjectWithSourceDTO objectWithSourceDTO) {
        appService.create(objectWithSourceDTO.getUri(), objectWithSourceDTO.getSource());
    }

    @PostMapping("/create")
    public void create(@RequestBody DTOs.ObjectWithContentDTO objectWithContentDTO) {
        appService.create(objectWithContentDTO.getUri(), objectWithContentDTO.getContent());
    }

    @GetMapping("/read")
    public byte[] read(@RequestBody DTOs.ObjectDTO objectDTO) {
        return appService.read(objectDTO.getUri());
    }

    @PutMapping("/update")
    public void update(@RequestBody DTOs.ObjectWithSourceDTO objectWithSourceDTO) {
        appService.update(objectWithSourceDTO.getUri(), objectWithSourceDTO.getSource());
    }

    @PutMapping("/update")
    public void update(@RequestBody DTOs.ObjectWithContentDTO objectWithContentDTO) {
        appService.update(objectWithContentDTO.getUri(), objectWithContentDTO.getContent());
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody DTOs.ObjectDTO objectDTO) {
        appService.delete(objectDTO.getUri());
    }

    @GetMapping("/publish")
    public String publish(@RequestBody DTOs.ObjectDTO objectDTO) {
        return appService.publish(objectDTO.getUri());
    }

    @GetMapping("/exists")
    public boolean exists(@RequestBody DTOs.ObjectDTO objectDTO) {
        return appService.exists(objectDTO.getUri());
    }
}
