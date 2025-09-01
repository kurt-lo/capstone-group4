package com.capstone.cargo.dto;

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
    private String originCity;
    private String originCountry;
    private Long destination;
    private String destinationCity;
    private String destinationCountry;
    private BigDecimal weight;
    private String containerSize;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

}
