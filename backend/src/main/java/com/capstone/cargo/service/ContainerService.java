package com.capstone.cargo.service;

import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.exception.ContainerNotFoundException;
import com.capstone.cargo.mapper.ContainerDTOMapper;
import com.capstone.cargo.model.Container;
import com.capstone.cargo.producer.KafkaProducer;
import com.capstone.cargo.repository.ContainerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.capstone.cargo.mapper.ContainerDTOMapper.*;

@Service
@Slf4j
public class ContainerService {
    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public ContainerDTO publishKafkaMessage(ContainerDTO containerDTO) {
        Container container = mapContainer(containerDTO);
        kafkaProducer.sendMessage(container);
        Container saved = containerRepository.save(container);
        return mapContainerDTO(saved);
    }

    public List<ContainerDTO> getAllContainers(String username, String role) {
        if ("ROLE_ADMIN".equals(role)) {
            List<Container> allContainers = containerRepository.findAll();
            if (allContainers.isEmpty()) {
                log.error("No containers found in the database");
                throw new ContainerNotFoundException("No containers found");
            }
            return allContainers.stream()
                    .map(ContainerDTOMapper::mapContainerDTO)
                    .toList();
        } else {
            List<Container> userContainers = containerRepository.findByCreatedBy(username);
            if (userContainers.isEmpty()) {
                log.error("No containers found for user: {}", username);
                throw new ContainerNotFoundException("No containers found for user: " + username);
            }
            return userContainers.stream()
                    .map(ContainerDTOMapper::mapContainerDTO)
                    .toList();
        }
    }

    public Optional<ContainerDTO> getContainerById(Long id) {
        return containerRepository.findByContainerId(id)
                .map(ContainerDTOMapper::mapContainerDTO)
                .or(() -> {
                    log.error("Container retrieval failed: Container ID: {} not found", id);
                    throw new ContainerNotFoundException("Container ID: " + id + " not found");
                });
    }

    public List<ContainerDTO> getContainersForReport(Long originId, Long destinationId, LocalDateTime startDate, LocalDateTime endDate, String username, String role) {
        if (isInvalidDate(startDate, endDate)) return Collections.emptyList();

        List<Container> container;

        if ("ROLE_ADMIN".equals(role)) {
            if (originId != null && destinationId != null) {
                 container = containerRepository.findByOriginAndDestination(originId, destinationId, startDate, endDate);
                return container.stream()
                        .map(ContainerDTOMapper::mapContainerDTO)
                        .toList();

            } else if (originId != null) {
                container  = containerRepository.findByOrigin(originId, startDate, endDate);
                return container.stream()
                        .map(ContainerDTOMapper::mapContainerDTO)
                        .toList();

            } else if (destinationId != null) {
                container = containerRepository.findByDestination(destinationId, startDate, endDate);
                return container.stream()
                        .map(ContainerDTOMapper::mapContainerDTO)
                        .toList();
            } else {
                container = containerRepository.findByDateRange(startDate, endDate);
                return container.stream()
                        .map(ContainerDTOMapper::mapContainerDTO)
                        .toList();
            }
        } else {
            if (originId != null && destinationId != null) {
                container = containerRepository.findByOriginAndDestinationForUser(username, originId, destinationId, startDate, endDate);

            } else if (originId != null) {
                container = containerRepository.findByOriginForUser(username, originId, startDate, endDate);
                return container.stream()
                        .map(ContainerDTOMapper::mapContainerDTO)
                        .toList();

            } else if (destinationId != null) {
                container = containerRepository.findByDestinationForUser(username, destinationId, startDate, endDate);
                return container.stream()
                        .map(ContainerDTOMapper::mapContainerDTO)
                        .toList();
            } else {
                container = containerRepository.findByDateRangeForUser(username, startDate, endDate);
                return container.stream()
                        .map(ContainerDTOMapper::mapContainerDTO)
                        .toList();
            }
        }
        return container.stream()
                .map(ContainerDTOMapper::mapContainerDTO)
                .toList();
    }


    public ContainerDTO createContainer(ContainerDTO containerDTO, String username) {
        Container container = mapContainer(containerDTO);
        container.setCreatedBy(username);
        Container saved = containerRepository.save(container);
        return mapContainerDTO(saved);
    }

    public ContainerDTO updateContainer(Long id, ContainerDTO containerDTO) {
        Container existingContainer = containerRepository.findByContainerId(id)
                .orElseThrow(() -> new ContainerNotFoundException("Container ID: " + id + " not found"));

        Container saved = containerRepository.save(updateContainerByDTO(existingContainer, containerDTO));
        return mapContainerDTO(saved);
    }

    public void deleteContainer(Long id) {
        if (!containerRepository.existsById(id)) {
            log.error("Container deletion failed: Container ID: {} not found", id);
            throw new ContainerNotFoundException("Container ID: " + id + " not found");
        }
        containerRepository.deleteById(id);
        log.info("Container deletion successful: Container ID: {}", id);
    }

    private static boolean isInvalidDate(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate == null || endDate == null || endDate.isBefore(startDate) || startDate.isAfter(endDate);
    }

}
