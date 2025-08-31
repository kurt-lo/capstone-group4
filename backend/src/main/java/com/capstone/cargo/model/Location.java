package com.capstone.cargo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCATION_ID")
    private long locationId;

    @Column(name = "LOCATION_NAME", nullable = false, length = 50)
    private String locationName;

    @Column(name = "LOCATION_TYPE", nullable = false, length = 10)
    private String locationType;

    @ManyToOne
    @JoinColumn(name = "CITY_ID", referencedColumnName = "CITY_ID")
    private City city;
}
