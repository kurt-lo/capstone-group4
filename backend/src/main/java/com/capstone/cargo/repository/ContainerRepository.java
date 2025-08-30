package com.capstone.cargo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.cargo.model.Container;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContainerRepository extends JpaRepository <Container, Long>{

    @Query("SELECT c FROM Container c " +
            "WHERE (:containerType IS NULL OR c.containerType = :containerType) " +
            "AND (:owner IS NULL OR c.owner = :owner) " +
            "AND (:origin IS NULL OR c.origin = :origin) " +
            "AND (:destination IS NULL OR c.destination = :destination)")
    List<Container> searchContainers(
            @Param("containerType") String containerType,
            @Param("owner") String owner,
            @Param("origin") String origin,
            @Param("destination") String destination
    );

}
