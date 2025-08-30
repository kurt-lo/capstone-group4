package com.capstone.cargo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capstone.cargo.model.Container;
import com.capstone.cargo.service.ContainerService;

@RestController
@RequestMapping ("/api/containers")
@CrossOrigin(origins = "http://localhost:5173")
public class ContainerController {

    @Autowired
    private ContainerService containerService;
    
    @GetMapping
    public ResponseEntity<List<Container>> getContainers(){
        List<Container> getAll = containerService.getAll();
        return new ResponseEntity<>(getAll, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Container>> search(
            @RequestParam(required = false) String containerType,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination
    ) {
        List<Container> results = containerService.search(containerType, owner, origin, destination);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

    @PostMapping("/add")
    public ResponseEntity<Container> addContainer(@RequestBody Container container){
        Container newContainer = containerService.addContainer(container);
        return new ResponseEntity<>(newContainer, HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<Container> createContainer(@RequestBody Container container){
        Container newContainer = containerService.createContainer(container);
        return new ResponseEntity<>(newContainer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Container> updateContainerItem(@PathVariable Long id, @RequestBody Container containerItem) {
        Container updatedContainer = containerService.updateContainer(id, containerItem);
        return ResponseEntity.ok(updatedContainer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContainer(@PathVariable Long id){
        return containerService.deleteContainer(id) ?
                new ResponseEntity<>("Container deleted", HttpStatus.OK)
                : new ResponseEntity<>("Container not found", HttpStatus.NOT_FOUND);
    }
}
 