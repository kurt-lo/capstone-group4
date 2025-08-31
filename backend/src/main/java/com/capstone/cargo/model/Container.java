package com.capstone.cargo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONTAINER")
public class Container extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTAINER_ID")
    private long containerId;

    @Column(name = "CONTAINER_TYPE", nullable = false, length = 20)
    private String containerType;

    @ManyToOne
    @JoinColumn(name = "ORIGIN", referencedColumnName = "CITY_ID")
    private City origin;

    @ManyToOne
    @JoinColumn(name = "DESTINATION", referencedColumnName = "CITY_ID")
    private City destination;

    @Column(name = "WEIGHT", nullable = true, precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "CONTAINER_SIZE", nullable = true, length = 10)
    private String containerSize;

    @Column(name = "DEPARTURE_DATE")
    private LocalDateTime departureDate;

    @Column(name = "ARRIVAL_DATE")
    private LocalDateTime arrivalDate;
}





