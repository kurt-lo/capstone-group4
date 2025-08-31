package com.capstone.cargo.service;

import com.capstone.cargo.dto.TrackingEventDTO;
import com.capstone.cargo.mapper.TrackingEventDTOMapper;
import com.capstone.cargo.model.TrackingEvent;
import com.capstone.cargo.repository.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.capstone.cargo.mapper.TrackingEventDTOMapper.mapTrackingEvent;
import static com.capstone.cargo.mapper.TrackingEventDTOMapper.mapTrackingEventDTO;

@Service
public class TrackingEventService {
    @Autowired
    private TrackingEventRepository trackingEventRepository;

    public List<TrackingEventDTO> getAllTrackingEvents() {
        return trackingEventRepository.findAll().stream()
                .map(TrackingEventDTOMapper::mapTrackingEventDTO)
                .toList();
    }

    public TrackingEventDTO createTrackingEvent(TrackingEventDTO trackingEventDTO) {
        TrackingEvent trackingEvent = mapTrackingEvent(trackingEventDTO);

        TrackingEvent saved = trackingEventRepository.save(trackingEvent);
        return mapTrackingEventDTO(saved);
    }
}
