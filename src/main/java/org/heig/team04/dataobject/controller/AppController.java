package org.heig.team04.dataobject.controller;

import org.heig.team04.dataobject.dto.DTOs;
import org.heig.team04.dataobject.service.ServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data-object")
public class AppController {

    private final ServiceInterface service;

    public AppController(ServiceInterface service) {
        this.service = service;
    }

    @PostMapping("/create-with-source")
    public ResponseEntity<String> create(@RequestBody DTOs.UriWithSourceDTO uriWithSourceDTO) {
        try {
            service.create(uriWithSourceDTO.getUri(), uriWithSourceDTO.getSource());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-with-content")
    public ResponseEntity<String> create(@RequestBody DTOs.UriWithContentDTO uriWithContentDTO) {
        try {
            service.create(uriWithContentDTO.getUri(), uriWithContentDTO.getContent());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<DTOs.ContentDTO> read(@RequestParam DTOs.UriDTO uriDTO) {
        byte[] content;
        try {
            content = service.read(uriDTO.getUri());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return ResponseEntity.ok(new DTOs.ContentDTO(content));
    }

    @PutMapping("/update-with-source")
    public ResponseEntity<String> update(@RequestBody DTOs.UriWithSourceDTO uriWithSourceDTO) {
        try {
            service.update(uriWithSourceDTO.getUri(), uriWithSourceDTO.getSource());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Object not found");
        }
        return ResponseEntity.ok("Object updated");
    }

    @PutMapping("/update-with-content")
    public ResponseEntity<String> update(@RequestBody DTOs.UriWithContentDTO uriWithContentDTO) {
        try {
            service.update(uriWithContentDTO.getUri(), uriWithContentDTO.getContent());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok("Object updated");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String uri, @RequestParam(defaultValue = "false") boolean ttl) {
        try {
            service.delete(uri, ttl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok("Object deleted");
    }

    @GetMapping("/publish")
    public ResponseEntity<DTOs.LinkDTO> publish(@RequestParam String uri, @RequestParam(defaultValue = "1800") int ttl) {
        String link;
        try {
            link = service.publish(uri, ttl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new DTOs.LinkDTO("Object not found"));
        }
        return ResponseEntity.ok(new DTOs.LinkDTO(link));
    }

    @GetMapping("/exists")
    public ResponseEntity<String> exists(@RequestParam String uri) {
        boolean exists;
        try {
            exists = service.exists(uri);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(String.valueOf(exists));
    }
}
