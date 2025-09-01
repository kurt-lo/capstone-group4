package com.capstone.cargo.service;

import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.exception.ContainerNotFoundException;
import com.capstone.cargo.mapper.ContainerDTOMapper;
import com.capstone.cargo.model.BaseEntity;
import com.capstone.cargo.model.City;
import com.capstone.cargo.model.Container;
import com.capstone.cargo.model.User;
import com.capstone.cargo.producer.KafkaProducer;
import com.capstone.cargo.repository.ContainerRepository;
import com.capstone.cargo.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContainerServiceTest {

    @InjectMocks
    private ContainerService containerService;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ContainerRepository containerRepository;

    @Mock
    private ContainerDTOMapper containerDTOMapper;

    @Mock
    private BaseEntity baseEntity;

    @Mock
    private City manila, cebu;

    private Container container, container2, container1, updatedData;

    private User user, admin;

    private ContainerDTO inputDto, outputDto;

    @BeforeEach
    public void setUp() {

        container = new Container();
        container.setContainerId(1L);
        container.setContainerType("General");
        container.setOrigin(manila); //1L,"Manila","PH"
        container.setDestination(cebu); //100005,"Cebu","PH"
        container.setWeight(BigDecimal.ONE);
        container.setCreatedBy("username");

        container2 = new Container();
        container2.setContainerId(2L);
        container2.setContainerType("Dangerous");
        container2.setOrigin(cebu);
        container2.setDestination(manila);
        container2.setWeight(BigDecimal.ONE);
        container2.setCreatedBy("anotherUser");

        container1 = new Container();
        container1.setContainerId(3L);
        container1.setContainerType("Dangerous");
        container1.setOrigin(cebu);
        container1.setDestination(manila);
        container1.setWeight(BigDecimal.ONE);
        container1.setCreatedBy("anotherUser2");

        updatedData = new Container();
        updatedData.setContainerType("Reefer");
        updatedData.setOrigin(cebu);
        updatedData.setDestination(manila);

        user = new User();
        user.setUsername("username");
        user.setRole(Role.USER);

        admin = new User();
        admin.setUsername("admin");
        admin.setRole(Role.ADMIN);
    }

    @Test
    void test_givenValidContainer_whenPublishKafkaMessage_thenReturnSavedContainerDTO() {
        ContainerDTO inputDto = new ContainerDTO();
        Container containerEntity = new Container();
        ContainerDTO outputDto = new ContainerDTO();

        try (MockedStatic<ContainerDTOMapper> mockedMapper = Mockito.mockStatic(ContainerDTOMapper.class)) {
            mockedMapper.when(() -> ContainerDTOMapper.mapContainer(any(ContainerDTO.class)))
                    .thenReturn(containerEntity);
            mockedMapper.when(() -> ContainerDTOMapper.mapContainerDTO(any(Container.class)))
                    .thenReturn(outputDto);

            when(containerRepository.save(any(Container.class))).thenReturn(containerEntity);

            ContainerDTO result = containerService.publishKafkaMessage(inputDto);

            assertNotNull(result);
            verify(kafkaProducer, times(1)).sendMessage(containerEntity);
            verify(containerRepository, times(1)).save(containerEntity);
        }
    }

    @Test
    void test_givenContainers_whenGetAllContainersAsAdmin_thenReturnContainerList() {
        when(containerRepository.findAll()).thenReturn(List.of(container, container2, container1));

        List<ContainerDTO> containers = containerService.getAllContainers(user.getUsername(), "ROLE_ADMIN");

        assertNotNull(containers);
        assertEquals(3, containers.size());
        verify(containerRepository, times(1)).findAll();
    }

    @Test
    void test_givenContainers_whenGetAllContainersAsUser_thenReturnContainerList() {
        when(containerRepository.findByCreatedBy(user.getUsername())).thenReturn(List.of(container));

        List<ContainerDTO> containers = containerService.getAllContainers(user.getUsername(), "ROLE_USER");

        assertNotNull(containers);
        assertEquals(1, containers.size());
        verify(containerRepository, times(1)).findByCreatedBy(user.getUsername());
    }

    @Test
    void test_givenNoContainer_whenGetAllContainersAsAdmin_thenThrowException() {
        when(containerRepository.findAll()).thenReturn(Collections.emptyList());

        ContainerNotFoundException exception = assertThrows(ContainerNotFoundException.class, () ->
                containerService.getAllContainers(admin.getUsername(), "ROLE_ADMIN"));

        assertEquals("No containers found", exception.getMessage());
        verify(containerRepository, times(1)).findAll();
    }


    @Test
    void test_givenNoContainers_whenGetAllContainersAsUser_thenThrowException() {
        when(containerRepository.findByCreatedBy(user.getUsername())).thenReturn(Collections.emptyList());

        ContainerNotFoundException exception = assertThrows(ContainerNotFoundException.class, () ->
                containerService.getAllContainers(user.getUsername(), "ROLE_USER"));

        assertEquals("No containers found for user: " + user.getUsername(), exception.getMessage());
        verify(containerRepository, times(1)).findByCreatedBy(user.getUsername());
    }

    @Test
    void test_givenContainer_whenGetContainerById_thenReturnContainer() {
        when(containerRepository.findByContainerId(1L)).thenReturn(Optional.of(container));

        Optional<ContainerDTO> retrievedContainer = containerService.getContainerById(1L);

        assertTrue(retrievedContainer.isPresent());
        assertEquals(1L, retrievedContainer.get().getContainerId());
        verify(containerRepository, times(1)).findByContainerId(1L);
    }

    @Test
    void test_givenInvalidContainerId_whenGetContainerById_thenThrowException() {
        when(containerRepository.findByContainerId(4L)).thenReturn(Optional.empty());

        ContainerNotFoundException exception = assertThrows(ContainerNotFoundException.class, () ->
                containerService.getContainerById(4L));

        assertEquals("Container ID: 4 not found", exception.getMessage());
        verify(containerRepository, times(1)).findByContainerId(4L);
    }

    @Test
    void test_givenValidContainer_whenCreateContainer_thenReturnContainer() {
        when(containerRepository.save(container)).thenReturn(container);

        ContainerDTO createdContainer = containerService.createContainer(ContainerDTOMapper.mapContainerDTO(container), user.getUsername());

        assertNotNull(createdContainer);
        assertEquals("username", createdContainer.getCreatedBy());
        verify(containerRepository, times(1)).save(container);
    }

    @Test
    void test_givenValidContainerIdAndData_whenUpdateContainer_thenReturnUpdatedContainer() {
        when(containerRepository.findByContainerId(1L)).thenReturn(Optional.of(container));
        when(containerRepository.save(any(Container.class))).thenReturn(updatedData);

        ContainerDTO updatedContainer = containerService.updateContainer(1L, ContainerDTOMapper.mapContainerDTO(updatedData));

        assertNotNull(updatedContainer);
        assertEquals("Reefer", updatedContainer.getContainerType());

        verify(containerRepository, times(1)).findByContainerId(1L);
        verify(containerRepository, times(1)).save(any(Container.class));
    }

    @Test
    void test_givenInvalidContainerId_whenUpdateContainer_thenThrowException() {
        when(containerRepository.findByContainerId(4L)).thenReturn(Optional.empty());

        ContainerNotFoundException exception = assertThrows(ContainerNotFoundException.class, () ->
                containerService.updateContainer(4L, ContainerDTOMapper.mapContainerDTO(updatedData)));

        assertEquals("Container ID: 4 not found", exception.getMessage());
        verify(containerRepository, times(1)).findByContainerId(4L);
        verify(containerRepository, times(0)).save(any(Container.class));
    }

    @Test
    void test_givenExistingContainerId_whenDeleteContainer_thenVerifyDeletion() {
        when(containerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(containerRepository).deleteById(1L);

        containerService.deleteContainer(1L);

        verify(containerRepository, times(1)).existsById(1L);
        verify(containerRepository, times(1)).deleteById(1L);
    }

    @Test
    void test_givenNonExistingContainerId_whenDeleteContainer_thenThrowException() {
        when(containerRepository.existsById(4L)).thenReturn(false);

        ContainerNotFoundException exception = assertThrows(ContainerNotFoundException.class, () ->
                containerService.deleteContainer(4L));

        assertEquals("Container ID: 4 not found", exception.getMessage());
        verify(containerRepository, times(1)).existsById(4L);
        verify(containerRepository, times(0)).deleteById(4L);
    }

    @Test
    void test_givenValidDateRange_whenGetContainersForReport_thenReturnContainerList() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now();

        when(containerRepository.findByOrigin(1L, startDate, endDate)).thenReturn(List.of(container, container2));

        List<ContainerDTO> containers = containerService.getContainersForReport(1L, startDate, endDate);

        assertNotNull(containers);
        assertEquals(2, containers.size());
        verify(containerRepository, times(1)).findByOrigin(1L, startDate, endDate);
    }

    @Test
    void test_givenValidDateRangeNoContainer_whenGetContainersForReport_thenReturnEmptyList() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now();

        when(containerRepository.findByOrigin(3L, startDate, endDate)).thenReturn(Collections.emptyList());

        List<ContainerDTO> containers = containerService.getContainersForReport(3L, startDate, endDate);

        assertNotNull(containers);
        verify(containerRepository, times(1)).findByOrigin(3L, startDate, endDate);
    }

    @Test
    void test_givenInvalidDateRange_whenGetContainersForReport_thenReturnEmptyList() {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        List<ContainerDTO> containers = containerService.getContainersForReport(null, startDate, endDate);

        assertTrue(containers.isEmpty());
        verify(containerRepository, never()).findByOrigin(4L, startDate, endDate);
    }

}
