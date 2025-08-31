package com.capstone.cargo.controller;

import com.capstone.cargo.dto.TrackingEventDTO;
import com.capstone.cargo.service.TrackingEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trackingEvent")
@CrossOrigin(origins = "http://localhost:5173")
public class TrackingEventController {
    @Autowired
    private TrackingEventService trackingEventService;

    @GetMapping()
    public ResponseEntity<List<TrackingEventDTO>> getTrackingEvents() {
        List<TrackingEventDTO> getAllTrackingEvents = trackingEventService.getAllTrackingEvents();
        return new ResponseEntity<>(getAllTrackingEvents, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<TrackingEventDTO> createContainer(@Valid @RequestBody TrackingEventDTO trackingEventDTO) {
          TrackingEventDTO newTrackingEvent = trackingEventService.createTrackingEvent(trackingEventDTO);
        return new ResponseEntity<>(newTrackingEvent, HttpStatus.CREATED);
    }
}
