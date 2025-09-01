package com.capstone.cargo.service;

import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.mapper.ContainerDTOMapper;
import com.capstone.cargo.model.Container;
import com.capstone.cargo.enums.TrackingEventTypes;
import com.capstone.cargo.producer.KafkaProducer;
import com.capstone.cargo.repository.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.capstone.cargo.mapper.ContainerDTOMapper.*;

@Service
public class ContainerService {
    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public List<ContainerDTO> getAllContainers() {
        return containerRepository.findAll().stream()
                .map(ContainerDTOMapper::mapContainerDTO)
                .toList();
    }

    public Optional<ContainerDTO> getContainerById(Long id) {
        return containerRepository.findByContainerId(id)
                .map(ContainerDTOMapper::mapContainerDTO);
    }

    public List<ContainerDTO> getContainersByDayRange(Long locationId, LocalDateTime startDate, LocalDateTime endDate) {
        if(isDateRangeValid(locationId, startDate, endDate)) return Collections.emptyList();

        return containerRepository.findByDate(locationId, startDate, endDate)
                .stream().filter(Objects::nonNull)
                .map(ContainerDTOMapper::mapContainerDTO)
                .toList();
    }
    public ContainerDTO createContainer(ContainerDTO containerDTO) {
        Container container = mapContainer(containerDTO);

        Container saved = containerRepository.save(container);
        return mapContainerDTO(saved);
    }

    public ContainerDTO updateContainer(Long id, ContainerDTO containerDTO) {
        Container existingContainer = containerRepository.findByContainerId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID does not exist"));

        Container saved = containerRepository.save(updateContainerByDTO(existingContainer, containerDTO));
        return mapContainerDTO(saved);
    }

    public boolean deleteContainer(Long id) {
        if(containerRepository.existsById(id)) {
            containerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private static boolean isDateRangeValid(Long locationId, LocalDateTime startDate, LocalDateTime endDate) {
        return locationId == null || startDate == null || endDate == null;
    }

    public List<Container> search(
            String containerType,
            Long originId,
            Long destinationId,
            TrackingEventTypes status
    ) {
        return containerRepository.searchContainer(containerType, originId, destinationId, status);
    }

}
