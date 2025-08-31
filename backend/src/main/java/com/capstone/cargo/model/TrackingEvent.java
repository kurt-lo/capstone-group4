package com.capstone.cargo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private long eventId;

    @Column(name = "EVENT_TYPE", nullable = true, length = 10)
    private String eventType;

    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "CONTAINER_ID", referencedColumnName = "CONTAINER_ID")
    private Container container;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "LOCATION_ID")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "NEXT_LOCATION_ID", referencedColumnName = "LOCATION_ID")
    private Location nextLocation;

    @ManyToOne
    @JoinColumn(name = "PREVIOUS_LOCATION_ID", referencedColumnName = "LOCATION_ID")
    private Location previousLocation;
}
