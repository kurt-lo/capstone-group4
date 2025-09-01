package com.capstone.cargo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.capstone.cargo.enums.ContainerStatus;
import org.springframework.stereotype.Service;
import com.capstone.cargo.producer.KafkaProducer;
import com.capstone.cargo.model.Container;
import com.capstone.cargo.repository.ContainerRepository;

@Service
public class ContainerService {

    private final ContainerRepository containerRepository;
    private final KafkaProducer kafkaProducer;

    public ContainerService(ContainerRepository containerRepository, KafkaProducer kafkaProducer) {
        this.containerRepository = containerRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<Container> getAll() {
        return containerRepository.findAll();
    }

    public Container addContainer(Container container) {
        Container newContainer = containerRepository.save(container);
        kafkaProducer.sendMessage(newContainer);
        return newContainer;
    }

    public Container createContainer(Container container) {
        return containerRepository.save(container);
    }

    public Optional<Container> getContainerById(Long id) {
        return containerRepository.findById(id);
    }

    public Container updateContainer(Long id, Container updatedContainer) {
        return containerRepository.findById(id)
                .map(existingContainer -> {
                    existingContainer.setContainerType(updatedContainer.getContainerType());
                    existingContainer.setOwner(updatedContainer.getOwner());
                    existingContainer.setDestination(updatedContainer.getDestination());
                    existingContainer.setOrigin(updatedContainer.getOrigin());
                    existingContainer.setStatus(updatedContainer.getStatus());
                    existingContainer.setMovementDate(updatedContainer.getMovementDate());
                    return containerRepository.save(existingContainer);
                })
                .orElseThrow(() -> new IllegalArgumentException("ID does not exist"));
    }

    public boolean deleteContainer(Long id) {
        if (containerRepository.existsById(id)) {
            containerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Container> search(
            String containerType,
            String owner,
            String origin,
            String destination,
            ContainerStatus status,
            LocalDate date
    ) {
        return containerRepository.searchContainer(containerType, owner, origin, destination, status, date);
    }
}

