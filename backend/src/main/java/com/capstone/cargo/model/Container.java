package com.capstone.cargo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Container extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTAINER_ID")
    private long containerId;

    @Column(name = "CONTAINER_NUMBER", nullable = false, length = 20)
    private String containerNumber;

    @Column(name = "CONTAINER_TYPE", nullable = false, length = 20)
    private String containerType;

    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;

    @Column(name = "ORIGIN", nullable = true, length = 10)
    private long origin;

    @Column(name = "DESTINATION", nullable = true, length = 10)
    private long destination;

    @Column(name = "WEIGHT", nullable = true, precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "CONTAINER_SIZE", nullable = true, length = 10)
    private String containerSize;

//    @Column(name = "PORT_OF_LOADING", nullable = true, length = 10)
//    private long portOfLoading;
//
//    @Column(name = "PORT_OF_DISCHARGE", nullable = true, length = 10)
//    private long portOfDischarge;

    @Column(name = "IS_IN_TRANSIT", nullable = true, length = 1)
    private Boolean isInTransit;

}





