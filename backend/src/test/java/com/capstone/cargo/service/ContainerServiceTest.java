package com.capstone.cargo.service;

import com.capstone.cargo.model.Container;
import com.capstone.cargo.repository.ContainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContainerServiceTest {

    @InjectMocks
    private ContainerService containerService;

    @Mock
    private ContainerRepository containerRepository;

    private Container container;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        container = new Container();
        container.setContainerId(1L);
        container.setContainerNumber("OOLU0001");
        container.setStatus("lOADED");
        container.setOrigin(100001);
        container.setDestination(100005);
        container.setWeight(BigDecimal.ONE);
        container.setContainerSize("40GP");
        container.setIsInTransit(Boolean.TRUE);
    }

    @Test
    void test_givencontainers_whenGetAllContainers_thenReturnContainerList() {
        when(containerRepository.findAll()).thenReturn(Collections.singletonList(container));

        assertThat(getAllContainers()).hasSize(1);
        verify(containerRepository, times(1)).findAll();
    }

    @Test
    void test_givenNoContainers_whenGetAllContainers_thenReturnEmptyList() {
        when(containerRepository.findAll()).thenReturn(Collections.emptyList());

        assertThat(getAllContainers()).isEmpty();
        verify(containerRepository, times(1)).findAll();
    }

    private List<Container> getAllContainers() {
        return containerService.getAll();
    }

    @Test
    void test_givenContainer_whenCreateContainer_thenReturnSavedContainer() {
        when(containerRepository.save(container)).thenReturn(container);

        assertThat(getSavedContainer(container)).isNotNull();
        verify(containerRepository, times(1)).save(container);
    }

    @Test
    void test_givenInvalidContainer_whenCreateContainer_thenReturnNull() {
        when(containerRepository.save(null)).thenReturn(null);

        assertThat(getSavedContainer(null)).isNull();
        verify(containerRepository, times(1)).save(null);
    }

    private Container getSavedContainer(Container container) {
        return containerService.createContainer(container);
    }

    @Test
    void test_givenContainerId_whenGetContainerById_thenReturnContainer() {
        when(containerRepository.findById(1L)).thenReturn(Optional.of(container));

        assertThat(getContainerById(1L)).isPresent();
        verify(containerRepository,  times(1)).findById(1L);
    }

    @Test
    void test_givenInvalidContainerId_whenGetContainerById_thenReturnEmpty() {
        when(containerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThat(getContainerById(2L)).isNotPresent();
        verify(containerRepository, times(1)).findById(2L);
    }

    private Optional<Container> getContainerById(Long id) {
        return containerService.getContainerById(id);
    }

    @Test
    void test_givenUpdatedContainerData_whenUpdateContainer_thenReturnUpdatedContainer() {
        Container updatedData = new Container();
        updatedData.setStatus("IN-TRANSIT");
        updatedData.setOrigin(100002);
        updatedData.setDestination(100006);

        when(containerRepository.findById(1L)).thenReturn(Optional.of(container));
        when(containerRepository.save(any(Container.class))).thenReturn(updatedData);

        Container updatedContainer = containerService.updateContainer(1L, updatedData);

        assertThat(updatedContainer.getStatus()).isEqualTo("IN-TRANSIT");
        assertThat(updatedContainer.getOrigin()).isEqualTo(100002);
        assertThat(updatedContainer.getDestination()).isEqualTo(100006);
        verify(containerRepository, times(1)).findById(1L);
        verify(containerRepository, times(1)).save(any(Container.class));
    }

    @Test
    void test_givenInvalidContainerId_whenUpdateContainer_thenThrowException() {
        Container updatedData = new Container();
        updatedData.setStatus("IN-TRANSIT");

        when(containerRepository.findById(2L)).thenReturn(Optional.empty());

        try {
            containerService.updateContainer(2L, updatedData);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("ID does not exist");
        }
        verify(containerRepository, times(1)).findById(2L);
        verify(containerRepository, times(0)).save(any(Container.class));
    }

    @Test
    void test_givenExistingContainerId_whenDeleteContainer_thenReturnTrue() {
        when(containerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(containerRepository).deleteById(1L);

        assertThat(deletedContainer(1L)).isTrue();
        verify(containerRepository, times(1)).existsById(1L);
        verify(containerRepository, times(1)).deleteById(1L);
    }

    @Test
    void test_givenNonExistingContainerId_whenDeleteContainer_thenReturnFalse() {
        when(containerRepository.existsById(2L)).thenReturn(false);

        assertThat(deletedContainer(2L)).isFalse();
        verify(containerRepository, times(1)).existsById(2L);
        verify(containerRepository, never()).deleteById(2L);
    }

    private boolean deletedContainer(Long id) {
        return containerService.deleteContainer(id);
    }
}
