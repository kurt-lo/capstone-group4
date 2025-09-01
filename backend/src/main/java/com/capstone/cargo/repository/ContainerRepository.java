package com.capstone.cargo.repository;

import com.capstone.cargo.model.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {

    Optional<Container> findByContainerId(Long id);
    List<Container> findByCreatedBy(String createdBy);

    @Query(value = "SELECT * FROM CONTAINER WHERE DESTINATION = :locationId AND CONTAINER_STATUS = 'RECEIVED' "
                + "AND DEPARTURE_DATE >= :startDate AND DEPARTURE_DATE < :endDate", nativeQuery = true)
    List<Container> findByDate(@Param("locationId") Long locationId, @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Container c " +
            "WHERE (:containerType IS NULL OR c.containerType = :containerType) " +
            "AND (:originId IS NULL OR c.origin.cityId = :originId) " +
            "AND (:destinationId IS NULL OR c.destination.cityId = :destinationId) ")
    List<Container> searchContainer(
            @Param("containerType") String containerType,
            @Param("originId") Long originId,
            @Param("destinationId") Long destinationId
    );

}
