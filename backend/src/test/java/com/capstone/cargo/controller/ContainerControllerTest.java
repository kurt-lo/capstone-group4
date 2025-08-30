package com.capstone.cargo.controller;

import com.capstone.cargo.model.Container;
import com.capstone.cargo.service.ContainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContainerController.class)
public class ContainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContainerService containerService;

    private Container testContainer;

    private Container invalidContainer;

    @BeforeEach
    void setUp() {
        testContainer = new Container(1L, "OOLU001", "Dangerous", "Unloaded",
                1000001, 1000005, BigDecimal.valueOf(123.45), "40GP", false);

        invalidContainer = new Container();
        invalidContainer.setContainerNumber("");
    }

    @Test
    void test_givenValidContainers_whenGetAllContainers_thenReturnContainerList() throws Exception {
        List<Container> containerList = List.of(
                testContainer,
                new Container(2L, "OOLU002", "General", "Loaded",
                        1000002, 1000005, BigDecimal.valueOf(123.45), "20GP", true)
        );
        when(containerService.getAll()).thenReturn(containerList);

        mockMvc.perform(get("/api/containers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].containerNumber").value("OOLU001"))
                .andExpect(jsonPath("$[1].containerNumber").value("OOLU002"));

        verify(containerService).getAll();
    }

    @Test
    void test_givenNoContainers_whenGetAllContainers_thenReturnEmptyList() throws Exception {
        when(containerService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/containers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.length()").value(0));

        verify(containerService).getAll();
    }

    @Test
    void test_givenValidContainer_whenCreateContainer_thenReturnCreatedContainer() throws Exception {
        when(containerService.createContainer(any(Container.class))).thenReturn(testContainer);

        mockMvc.perform(post("/api/containers/create").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(testContainer)))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(jsonPath("$.containerNumber").value("OOLU001"))
                .andExpect(jsonPath("$.containerId").value(1L));

        verify(containerService, times(1)).createContainer(any(Container.class));
    }

    @Test
    void test_givenInvalidContainer_whenCreateContainer_thenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/containers/create").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidContainer)))
                .andExpect(status().isBadRequest()).andDo(print());

        verify(containerService, never()).createContainer(any());
    }

    @Test
    void test_givenValidUpdateContainerData_whenUpdateContainer_thenReturnUpdatedContainer() throws Exception {
        Container updatedContainer = new Container(1L, "OOLU001", "Dangerous", "Loaded", 1000001, 1000005, BigDecimal.valueOf(123.45) , "40GP", true);

        when(containerService.updateContainer(eq(1L), any(Container.class))).thenReturn(updatedContainer);

        mockMvc.perform(put("/api/containers/1").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedContainer)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.status").value("Loaded"));

        verify(containerService, times(1)).updateContainer(eq(1L), any());
    }

    @Test
    void test_givenInvalidUpdateContainerData_whenUpdateContainer_thenReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/containers/1").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidContainer)))
                .andExpect(status().isBadRequest()).andDo(print());

        verify(containerService, never()).updateContainer(eq(1L), any(Container.class));
    }

    @Test
    void test_givenValidContainerId_whenDeleteContainer_thenReturnDeleteContainer() throws Exception {
        when(containerService.deleteContainer(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/containers/1"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().string("Container deleted"));

        verify(containerService, times(1)).deleteContainer(1L);
    }

    @Test
    void test_givenInvalidContainerId_whenDeleteContainer_thenReturnNotFoundContainer() throws Exception {
        when(containerService.deleteContainer(3L)).thenReturn(false);

        mockMvc.perform(delete("/api/containers/3"))
                .andExpect(status().isNotFound()).andDo(print())
                .andExpect(content().string("Container not found"));

        verify(containerService, times(1)).deleteContainer(3L);
    }

}
