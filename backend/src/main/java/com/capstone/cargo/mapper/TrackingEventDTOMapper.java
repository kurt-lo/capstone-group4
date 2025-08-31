package com.capstone.cargo.mapper;

import com.capstone.cargo.dto.TrackingEventDTO;
import com.capstone.cargo.model.Container;
import com.capstone.cargo.model.TrackingEvent;

import static com.capstone.cargo.mapper.MapperDTOUtils.cityBuilder;
import static com.capstone.cargo.mapper.MapperDTOUtils.locationBuilder;

public class TrackingEventDTOMapper {

    private TrackingEventDTOMapper() {
    }

    public static TrackingEventDTO mapTrackingEventDTO(TrackingEvent trackingEvent) {
        TrackingEventDTO trackingEventDTO = new TrackingEventDTO();
        if(trackingEventDTO.getEventId() != null) {
            trackingEvent.setEventId(trackingEventDTO.getEventId());
        }

        trackingEventDTO.setEventId(trackingEvent.getEventId());
        trackingEventDTO.setEventType(trackingEvent.getEventType());
        trackingEventDTO.setEventDate(trackingEvent.getEventDate());
        trackingEventDTO.setContainer(trackingEvent.getContainer().getContainerId());
        trackingEventDTO.setLocation(trackingEvent.getLocation().getCityId());
        trackingEventDTO.setNextLocation(trackingEvent.getNextLocation().getCityId());
        trackingEventDTO.setPreviousLocation(trackingEvent.getPreviousLocation().getCityId());

        return trackingEventDTO;
    }

    public static TrackingEvent mapTrackingEvent(TrackingEventDTO trackingEventDTO) {
        TrackingEvent trackingEvent = new TrackingEvent();

        if(trackingEventDTO.getEventId() != null) {
            trackingEvent.setEventId(trackingEventDTO.getEventId());
        }
        Container container = new Container();
        container.setContainerId(trackingEventDTO.getContainer());

        trackingEvent.setEventType(trackingEventDTO.getEventType());
        trackingEvent.setEventDate(trackingEventDTO.getEventDate());
        trackingEvent.setContainer(container);
        trackingEvent.setLocation(cityBuilder(trackingEventDTO.getLocation()));
        trackingEvent.setNextLocation(cityBuilder(trackingEventDTO.getNextLocation()));
        trackingEvent.setPreviousLocation(cityBuilder(trackingEventDTO.getPreviousLocation()));

        return trackingEvent;
    }
}
