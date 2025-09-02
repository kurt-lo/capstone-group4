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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private LocalDateTime startDate, endDate;

    @BeforeEach
    public void setUp() {

        manila = new City();
        manila.setCityId(1L);
        manila.setCityName("Manila");

        cebu = new City();
        cebu.setCityId(2L);
        cebu.setCityName("Cebu");

        container = new Container();
        container.setContainerId(1L);
        container.setContainerType("General");
        container.setOrigin(manila); //1L,"Manila","PH"
        container.setDestination(cebu); //100005,"Cebu","PH"
        container.setWeight(BigDecimal.ONE);
        container.setCreatedBy("username");
        container.setCreateDate(LocalDateTime.now());

        container2 = new Container();
        container2.setContainerId(2L);
        container2.setContainerType("Dangerous");
        container2.setOrigin(cebu);
        container2.setDestination(manila);
        container2.setWeight(BigDecimal.ONE);
        container2.setCreatedBy("anotherUser");
        container2.setCreateDate(LocalDateTime.now());

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

        startDate = LocalDateTime.now();
        endDate = LocalDateTime.now();
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
        when(containerRepository.save(any(Container.class))).thenAnswer(invocation -> {
            Container saved = invocation.getArgument(0);
            saved.setContainerId(1L);
            return saved;
        });

        ContainerDTO createdContainer = containerService.createContainer(
                ContainerDTOMapper.mapContainerDTO(container),
                "username"
        );

        assertNotNull(createdContainer);
        assertEquals("username", createdContainer.getCreatedBy());
        verify(containerRepository, times(1)).save(any(Container.class));
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

    @ParameterizedTest
    @CsvSource({
            "ROLE_ADMIN, 1, 2, 2",   // admin with origin + destination → expect 2 containers
            "ROLE_ADMIN, 1, , 1",    // admin with origin only → expect 1
            "ROLE_ADMIN, , 2, 1",    // admin with destination only → expect 1
            "ROLE_ADMIN, , , 1",     // admin with no filters → expect 1
            "ROLE_USER, 1, 2, 2",    // user with origin + destination → expect 2
            "ROLE_USER, 1, , 1",     // user with origin only → expect 1
            "ROLE_USER, , 2, 1",     // user with destination only → expect 1
            "ROLE_USER, , , 1"       // user with no filters → expect 1
    })
    void test_givenValidParameters_whenGetContainersForReport_thenReturnContainerList(
            String role,
            Long originId,
            Long destinationId,
            int expectedSize
    ) {
        List<Container> containers = new ArrayList<>();
        for (int i = 0; i < expectedSize; i++) {
            Container mockContainer = new Container();
            mockContainer.setContainerId(i + 1);
            mockContainer.setOrigin(manila);
            mockContainer.setDestination(cebu);
            containers.add(mockContainer);
        }

        mockDependencies(containers, role, originId, destinationId);

        List<ContainerDTO> result = containerService.getContainersForReport(
                originId, destinationId, startDate, endDate, "testUser", role
        );

        assertNotNull(result);
        assertEquals(expectedSize, result.size());
        result.forEach(dto -> {
            assertEquals(1L, dto.getOrigin());
            assertEquals(2L, dto.getDestination());
        });
    }

    private void mockDependencies(List<Container> containers, String role, Long originId, Long destinationId) {
        boolean isAdmin = "ROLE_ADMIN".equals(role);

        if (originId != null && destinationId != null) {
            if (isAdmin) {
                when(containerRepository.findByOriginAndDestination(anyLong(), anyLong(), eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            } else {
                when(containerRepository.findByOriginAndDestinationForUser(anyString(), anyLong(), anyLong(), eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            }
        } else if (originId != null) {
            if (isAdmin) {
                when(containerRepository.findByOrigin(anyLong(), eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            } else {
                when(containerRepository.findByOriginForUser(anyString(), anyLong(), eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            }
        } else if (destinationId != null) {
            if (isAdmin) {
                when(containerRepository.findByDestination(anyLong(), eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            } else {
                when(containerRepository.findByDestinationForUser(anyString(), anyLong(), eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            }
        } else {
            if (isAdmin) {
                when(containerRepository.findByDateRange(eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            } else {
                when(containerRepository.findByDateRangeForUser(anyString(), eq(startDate), eq(endDate)))
                        .thenReturn(containers);
            }
        }
    }

    @Test
    void test_givenInvalidDateRange_whenGetContainersForReport_thenReturnEmptyList() {
        LocalDateTime invalidStart = LocalDateTime.of(2024, 2, 1, 0, 0);
        LocalDateTime invalidEnd = LocalDateTime.of(2024, 1, 1, 0, 0);

        List<ContainerDTO> result = containerService.getContainersForReport(100L, 200L, invalidStart, invalidEnd, "testUser", "ROLE_ADMIN");

        assertTrue(result.isEmpty());
        verifyNoInteractions(containerRepository);
    }

    @Test
    void test_givenNullLocationIds_whenGetContainersForReport_thenReturnAllContainersInDateRange() {
        when(containerRepository.findByDateRange(startDate, endDate)).thenReturn(Collections.emptyList());

        List<ContainerDTO> result = containerService.getContainersForReport(null, null, startDate, endDate, "testUser", "ROLE_ADMIN");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
