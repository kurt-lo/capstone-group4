package com.capstone.cargo.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.capstone.cargo.dto.ContainerDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<List<ContainerDTO>> getContainers(){
        List<ContainerDTO> getAll = containerService.getAllContainers();
        return new ResponseEntity<>(getAll, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContainerDTO> getContainerById(@PathVariable Long id){
        ContainerDTO container = containerService.getContainerById(id).orElse(null);
        return new ResponseEntity<>(container, HttpStatus.OK);
    }

    @GetMapping("/day-range")
    public ResponseEntity<List<ContainerDTO>> getContainersByDayRange(
            @RequestParam Long locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        List<ContainerDTO> containers = containerService.getContainersByDayRange(locationId, startDate, endDate);

        return containers.isEmpty() ?
                ResponseEntity.noContent().build()
                : new ResponseEntity<>(containers, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ContainerDTO> createContainer(@Valid @RequestBody ContainerDTO container){
        ContainerDTO newContainer = containerService.createContainer(container);
        return new ResponseEntity<>(newContainer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContainerDTO> updateContainerItem(@PathVariable Long id, @Valid @RequestBody ContainerDTO containerItem) {
        ContainerDTO updatedContainer = containerService.updateContainer(id, containerItem);
        return ResponseEntity.ok(updatedContainer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContainer(@PathVariable Long id){
        return containerService.deleteContainer(id) ?
                new ResponseEntity<>("Container deleted", HttpStatus.OK)
                : new ResponseEntity<>("Container not found", HttpStatus.NOT_FOUND);
    }
}
 