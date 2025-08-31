package com.capstone.cargo.repository;

import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.model.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContainerRepository extends JpaRepository<Container, Long> {

    Optional<Container> findByContainerId(Long id);

    @Query(value = "SELECT * FROM CONTAINER WHERE DESTINATION = :locationId AND CONTAINER_STATUS = 'RECEIVED' AND DEPARTURE_DATE >= :startDate AND DEPARTURE_DATE < :endDate", nativeQuery = true)
    List<Container> findByDate(@Param("locationId") Long locationId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
