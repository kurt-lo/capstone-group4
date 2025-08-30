package com.capstone.cargo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capstone.cargo.model.Container;
import com.capstone.cargo.service.ContainerService;

import javax.validation.Valid;

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

    @PostMapping("/create")
    public ResponseEntity<Container> createContainer(@Valid @RequestBody Container container){
        Container newContainer = containerService.createContainer(container);
        return new ResponseEntity<>(newContainer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Container> updateContainerItem(@PathVariable Long id, @Valid @RequestBody Container containerItem) {
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
 