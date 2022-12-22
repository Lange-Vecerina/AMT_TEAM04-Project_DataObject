package org.heig.team04.dataobject.controller;

import org.heig.team04.dataobject.dto.SourceDTO;
import org.heig.team04.dataobject.service.ServiceInterface;
import org.heig.team04.dataobject.service.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * This class is the controller of the application.
 *
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
@RestController
@RequestMapping("/data-object")
public class AppController {
    // The service of the application
    private final ServiceInterface service;

    public AppController(ServiceInterface service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<String> create(@RequestParam String uri, @RequestBody SourceDTO source) {
        boolean success;
        try {
            if (source.getUrl().equals("")) {
                System.out.println("=================SUUUUUUUUUUUUUUUUUU");
                success = service.create(uri, source.getContent());
            } else {
                System.out.println("======================NOOOOOOOOOOOOOOOOOOOOOOOOOO");
                success = service.create(uri, source.getUrl());
            }
        } catch (InvalidURLException | AlreadyExistsException | URLNotAccessibleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExternalServiceException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        if (success) {
            return ResponseEntity.ok("Object created");
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/content")
    public ResponseEntity<String> read(@RequestParam String uri) {
        byte[] content;

        try {
            content = service.read(uri);
        } catch (NotFoundException | NotAnObjectException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExternalServiceException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body(Arrays.toString(content));
    }

    @PutMapping("")
    public ResponseEntity<String> update(@RequestParam String uri, @RequestBody SourceDTO source) {
        boolean success;
        try {
            if (source.getUrl().equals("")) {
                success = service.update(uri, source.getContent());
            } else {
                success = service.update(uri, source.getUrl());
            }
        } catch (NotFoundException | NotAnObjectException | InvalidURLException | URLNotAccessibleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExternalServiceException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        if (success) {
            return ResponseEntity.ok("Object updated");
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("")
    public ResponseEntity<String> delete(@RequestParam String uri, @RequestParam(defaultValue = "false") boolean recursive) {
        boolean success;
        try {
            success = service.delete(uri, recursive);
        } catch (NotFoundException | DeleteCollectionNoRecursiveException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExternalServiceException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        if (success) {
            return ResponseEntity.ok("Object deleted");
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/link")
    public ResponseEntity<String> publish(@RequestParam String uri, @RequestParam(defaultValue = "1800") int ttl) {
        String link = "";
        try {
            link = service.publish(uri, ttl);
        } catch (NotFoundException | NotAnObjectException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExternalServiceException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(link);
    }

    @GetMapping("")
    public ResponseEntity<String> exists(@RequestParam String uri) {
        boolean exists;
        try {
            exists = service.exists(uri);
        } catch (ExternalServiceException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(String.valueOf(exists));
    }
}
