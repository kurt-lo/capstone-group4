package com.capstone.cargo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

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

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "CONTAINER_ID", referencedColumnName = "CONTAINER_ID")
    private Container container;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "CITY_ID")
    private City location;

    @ManyToOne
    @JoinColumn(name = "NEXT_LOCATION_ID", referencedColumnName = "CITY_ID")
    private City nextLocation;

    @ManyToOne
    @JoinColumn(name = "PREVIOUS_LOCATION_ID", referencedColumnName = "CITY_ID")
    private City previousLocation;
}
