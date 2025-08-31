package com.capstone.cargo.mapper;

import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.model.Container;
import com.capstone.cargo.model.Location;

import static com.capstone.cargo.mapper.MapperDTOUtils.cityBuilder;
import static com.capstone.cargo.mapper.MapperDTOUtils.locationBuilder;

public class ContainerDTOMapper {

    private ContainerDTOMapper() {
    }

    public static ContainerDTO mapContainerDTO(Container container) {
        ContainerDTO containerDTO = new ContainerDTO();

        containerDTO.setContainerId(container.getContainerId());
        containerDTO.setContainerType(container.getContainerType());
        containerDTO.setOrigin(container.getOrigin().getCityId());
        containerDTO.setDestination(container.getDestination().getCityId());
        containerDTO.setWeight(container.getWeight());
        containerDTO.setContainerSize(container.getContainerSize());
        containerDTO.setDepartureDate(container.getDepartureDate());
        containerDTO.setArrivalDate(container.getArrivalDate());
        containerDTO.setCreatedBy(container.getCreatedBy());
        containerDTO.setUpdatedBy(container.getUpdatedBy());
        containerDTO.setCreateDate(container.getCreateDate());
        containerDTO.setUpdatedDate(container.getUpdatedDate());

        return containerDTO;
    }

    public static Container mapContainer(ContainerDTO containerDTO) {
        Container container = new Container();

        if(containerDTO.getContainerId() != null) {
            container.setContainerId(containerDTO.getContainerId());
        }

        container.setContainerType(containerDTO.getContainerType());
        container.setOrigin(cityBuilder(containerDTO.getOrigin()));
        container.setDestination(cityBuilder(containerDTO.getDestination()));
        container.setWeight(containerDTO.getWeight());
        container.setContainerSize(containerDTO.getContainerSize());
        container.setDepartureDate(containerDTO.getDepartureDate());
        container.setArrivalDate(containerDTO.getArrivalDate());

        return container;
    }

    public static Container updateContainerByDTO(Container container, ContainerDTO containerDTO) {
        if(containerDTO.getContainerId() != null) {
            container.setContainerId(containerDTO.getContainerId());
        }
        container.setContainerType(containerDTO.getContainerType());
        container.setOrigin(cityBuilder(containerDTO.getOrigin()));
        container.setDestination(cityBuilder(containerDTO.getDestination()));
        container.setWeight(containerDTO.getWeight());
        container.setContainerSize(containerDTO.getContainerSize());
        container.setDepartureDate(containerDTO.getDepartureDate());
        container.setArrivalDate(containerDTO.getArrivalDate());

        return container;
    }
}
