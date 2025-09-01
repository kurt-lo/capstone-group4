package com.capstone.cargo.repository;

import com.capstone.cargo.enums.ContainerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.cargo.model.Container;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {

    @Query("SELECT c FROM Container c " +
            "WHERE (:containerType IS NULL OR c.containerType = :containerType) " +
            "AND (:owner IS NULL OR c.owner = :owner) " +
            "AND (:origin IS NULL OR c.origin = :origin) " +
            "AND (:destination IS NULL OR c.destination = :destination) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:movementDate IS NULL OR c.movementDate = :movementDate)")
    List<Container> searchContainer(
            @Param("containerType") String containerType,
            @Param("owner") String owner,
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("status") ContainerStatus status,
            @Param("movementDate") LocalDate movementDate
    );
}