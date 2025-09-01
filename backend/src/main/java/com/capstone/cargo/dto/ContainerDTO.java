package com.capstone.cargo.dto;

import com.capstone.cargo.Event.TrackingEventTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerDTO {
    private Long containerId;
    private String containerType;
    private Long origin;
    private String origin_city;
    private String origin_country;
    private Long destination;
    private String destination_city;
    private String destination_country;
    private BigDecimal weight;
    private String containerSize;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

}
