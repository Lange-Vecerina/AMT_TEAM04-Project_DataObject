package org.heig.team04.dataobject.controller;

import com.amazonaws.AmazonServiceException;
import org.heig.team04.dataobject.service.ServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.heig.team04.dataobject.dto.DTOs;

@RestController
@RequestMapping("/data-object")
public class AppController {

    private final ServiceInterface appService;

    public AppController(ServiceInterface appService) {
        this.appService = appService;
    }

    @PostMapping("/create-with-source")
    public ResponseEntity<String> create(@RequestBody DTOs.UriWithSourceDTO uriWithSourceDTO) {
        try {
            appService.create(uriWithSourceDTO.getUri(), uriWithSourceDTO.getSource());
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-with-content")
    public ResponseEntity<String> create(@RequestBody DTOs.UriWithContentDTO uriWithContentDTO) {
        try {
            appService.create(uriWithContentDTO.getUri(), uriWithContentDTO.getContent());
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<DTOs.ContentDTO> read(@RequestParam DTOs.UriDTO uriDTO) {
        byte[] content;
        try {
            content = appService.read(uriDTO.getUri());
        } catch (AmazonServiceException e) {
            return null;
        }
        return ResponseEntity.ok(new DTOs.ContentDTO(content));
    }

    @PutMapping("/update-with-source")
    public ResponseEntity<String> update(@RequestBody DTOs.UriWithSourceDTO uriWithSourceDTO) {
        try {
            appService.update(uriWithSourceDTO.getUri(), uriWithSourceDTO.getSource());
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(404).body("Object not found");
        }
        return ResponseEntity.ok("Object updated");
    }

    @PutMapping("/update-with-content")
    public ResponseEntity<String> update(@RequestBody DTOs.UriWithContentDTO uriWithContentDTO) {
        try {
            appService.update(uriWithContentDTO.getUri(), uriWithContentDTO.getContent());
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok("Object updated");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String uri, @RequestParam(defaultValue = "false") boolean ttl) {
        try {
            appService.delete(uri, ttl);
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok("Object deleted");
    }

    @GetMapping("/publish")
    public ResponseEntity<DTOs.LinkDTO> publish(@RequestParam String uri, @RequestParam(defaultValue = "1800") int ttl) {
        String link;
        try {
            link = appService.publish(uri, ttl);
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(404).body(new DTOs.LinkDTO("Object not found"));
        }
        return ResponseEntity.ok(new DTOs.LinkDTO(link));
    }

    @GetMapping("/exists")
    public ResponseEntity<String> exists(@RequestParam String uri) {
        boolean exists;
        try {
            exists = appService.exists(uri);
        } catch (AmazonServiceException e) {
            return ResponseEntity.badRequest().body(e.getStatusCode() + " " + e.getErrorMessage());
        }

        return ResponseEntity.ok().body(String.valueOf(exists));
    }
}
