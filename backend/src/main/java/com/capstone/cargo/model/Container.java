package com.capstone.cargo.model;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.math.BigDecimal;

@Entity
//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Container extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTAINER_ID")
    private long containerId;

    @NotBlank(message = "Container number must not be blank")
    @Column(name = "CONTAINER_NUMBER", nullable = false, length = 20)
    private String containerNumber;

    @NotBlank(message = "Container type must not be blank")
    @Column(name = "CONTAINER_TYPE", nullable = false, length = 20)
    private String containerType;

    @NotBlank(message = "Container status must not be blank")
    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;

//    @NotBlank(message = "Container Origin must not be blank")
    @Column(name = "ORIGIN", nullable = true, length = 10)
    private long origin;

//    @NotBlank(message = "Container Destination must not be blank")
    @Column(name = "DESTINATION", nullable = true, length = 10)
    private long destination;

//    @NotBlank(message = "Container Weight must not be blank")
    @Column(name = "WEIGHT", nullable = true, precision = 10, scale = 2)
    private BigDecimal weight;

//    @NotBlank(message = "Container Size must not be blank")
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





