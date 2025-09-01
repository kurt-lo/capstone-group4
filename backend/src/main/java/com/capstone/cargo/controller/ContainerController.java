package com.capstone.cargo.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.service.ContainerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<ContainerDTO>> getContainers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // username from JWT
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        List<ContainerDTO> containers = containerService.getAllContainers(username, role);
        return new ResponseEntity<>(containers, HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<Container> searchContainers(
            @RequestParam(required = false) String containerType,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) Long originId,
            @RequestParam(required = false) Long destinationId
    ) {
        return containerService.search(
                containerType,
                originId,
                destinationId
        );
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        ContainerDTO newContainer = containerService.createContainer(container, username);
        return new ResponseEntity<>(newContainer, HttpStatus.CREATED);
    }

    @PostMapping("/publish")
    public ResponseEntity<ContainerDTO> publishKafkaContainer(@RequestBody ContainerDTO container){
        ContainerDTO newContainer = containerService.publishKafkaMessage(container);
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
 