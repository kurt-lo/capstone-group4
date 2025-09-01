package com.capstone.cargo.service;

import com.capstone.cargo.dto.TrackingEventDTO;
import com.capstone.cargo.mapper.TrackingEventDTOMapper;
import com.capstone.cargo.model.TrackingEvent;
import com.capstone.cargo.repository.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.capstone.cargo.mapper.MapperDTOUtils.containerBuilder;
import static com.capstone.cargo.mapper.TrackingEventDTOMapper.*;

@Service
public class TrackingEventService {
    @Autowired
    private TrackingEventRepository trackingEventRepository;

    public List<TrackingEventDTO> getAllTrackingEvents() {
        return trackingEventRepository.findAll().stream()
                .map(TrackingEventDTOMapper::mapTrackingEventDTO)
                .toList();
    }

    public List<TrackingEventDTO> getTrackingEventsByContainerId(Long containerId) {
        return trackingEventRepository.findByContainerContainerId(containerId).stream()
                .map(TrackingEventDTOMapper::mapTrackingEventDTO)
                .toList();
    }

    public TrackingEventDTO createTrackingEvent(TrackingEventDTO trackingEventDTO) {
        String latestEvent = trackingEventRepository.findByLatestContainerEvent(trackingEventDTO.getContainer());

        if (trackingEventDTO.getEventType().equals(latestEvent)) {
            return null;
        }

        TrackingEvent saved = latestEvent != null ?
                trackingEventRepository.save(mapTrackingEvent(trackingEventDTO))
                : mapNewTrackingEvent(trackingEventDTO);

        return mapTrackingEventDTO(saved);
    }
}
