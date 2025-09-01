package com.capstone.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEventDTO {
    private Long eventId;
    private String eventType;
    private LocalDateTime eventDate;
    private Long container;
    private Long location;
    private Long nextLocation;
    private Long previousLocation;
    private String locationName;
    private String nextLocationName;
    private String previousLocationName;
}
