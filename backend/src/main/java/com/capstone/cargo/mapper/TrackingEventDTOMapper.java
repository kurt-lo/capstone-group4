package com.capstone.cargo.mapper;

import com.capstone.cargo.dto.TrackingEventDTO;
import com.capstone.cargo.model.Container;
import com.capstone.cargo.model.TrackingEvent;

import static com.capstone.cargo.enums.TrackingEventTypes.DISCHARGED;
import static com.capstone.cargo.mapper.MapperDTOUtils.cityBuilder;

public class TrackingEventDTOMapper {

    private TrackingEventDTOMapper() {
    }

    public static TrackingEventDTO mapTrackingEventDTO(TrackingEvent trackingEvent) {
        TrackingEventDTO trackingEventDTO = new TrackingEventDTO();
        trackingEventDTO.setEventId(trackingEvent.getEventId());
        trackingEventDTO.setEventType(trackingEvent.getEventType());
        trackingEventDTO.setEventDate(trackingEvent.getEventDate());
        trackingEventDTO.setContainer(trackingEvent.getContainer().getContainerId());
        trackingEventDTO.setLocationName(trackingEvent.getLocation() != null ? trackingEvent.getLocation().getCityName() : null);
        trackingEventDTO.setNextLocationName(trackingEvent.getNextLocation() != null ? trackingEvent.getNextLocation().getCityName() : null);
        trackingEventDTO.setPreviousLocationName(trackingEvent.getPreviousLocation() != null ? trackingEvent.getPreviousLocation().getCityName() : null);

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
        trackingEvent.setContainer(container);
        trackingEvent.setLocation(cityBuilder(trackingEventDTO.getLocation()));
        trackingEvent.setNextLocation(cityBuilder(trackingEventDTO.getNextLocation()));
        trackingEvent.setPreviousLocation(cityBuilder(trackingEventDTO.getPreviousLocation()));

        return trackingEvent;
    }
    public static TrackingEvent mapNewTrackingEvent(TrackingEventDTO trackingEventDTO) {
        trackingEventDTO.setEventType(DISCHARGED.name());
        return mapTrackingEvent(trackingEventDTO);
    }
}
