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

    // Fetch containers by origin
    @Query(value = "SELECT * " +
            "FROM CONTAINER " +
            "WHERE ORIGIN = :originId " +
            "AND DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByOrigin(@Param("originId") Long originId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);


    // Fetch containers by destination
    @Query(value = "SELECT * " +
            "FROM CONTAINER " +
            "WHERE DESTINATION = :destinationId " +
            "AND DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByDestination(@Param("destinationId") Long destinationId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);


    // Fetch containers within a date range (must have an IN_TRANSIT event)
    @Query(value = "SELECT DISTINCT C.* " +
            "FROM CONTAINER C " +
            "INNER JOIN TRACKING_EVENT TE ON C.CONTAINER_ID = TE.CONTAINER_ID " +
            "WHERE TE.EVENT_TYPE = 'IN_TRANSIT' " +
            "AND C.DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);


    // Fetch containers by origin + destination
    @Query(value = "SELECT * " +
            "FROM CONTAINER " +
            "WHERE ORIGIN = :originId " +
            "AND DESTINATION = :destinationId " +
            "AND DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByOriginAndDestination(@Param("originId") Long originId,
                                               @Param("destinationId") Long destinationId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);


    // Fetch containers by origin for a user
    @Query(value = "SELECT * " +
            "FROM CONTAINER " +
            "WHERE CREATED_BY = :username " +
            "AND ORIGIN = :originId " +
            "AND DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByOriginForUser(@Param("username") String username,
                                        @Param("originId") Long originId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);


    // Fetch containers by destination for a user
    @Query(value = "SELECT * " +
            "FROM CONTAINER " +
            "WHERE CREATED_BY = :username " +
            "AND DESTINATION = :destinationId " +
            "AND DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByDestinationForUser(@Param("username") String username,
                                             @Param("destinationId") Long destinationId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);


    // Fetch containers within a date range for a user (must have an IN_TRANSIT event)
    @Query(value = "SELECT DISTINCT C.* " +
            "FROM CONTAINER C " +
            "INNER JOIN TRACKING_EVENT TE ON C.CONTAINER_ID = TE.CONTAINER_ID " +
            "WHERE C.CREATED_BY = :username " +
            "AND TE.EVENT_TYPE = 'IN_TRANSIT' " +
            "AND C.DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByDateRangeForUser(@Param("username") String username,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);


    // Fetch containers by origin + destination for a user
    @Query(value = "SELECT * " +
            "FROM CONTAINER " +
            "WHERE CREATED_BY = :username " +
            "AND ORIGIN = :originId " +
            "AND DESTINATION = :destinationId " +
            "AND DEPARTURE_DATE BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Container> findByOriginAndDestinationForUser(@Param("username") String username,
                                                      @Param("originId") Long originId,
                                                      @Param("destinationId") Long destinationId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);


    @Query("SELECT c FROM Container c " +
            "WHERE " +
            "(:originId IS NULL OR c.origin.cityId = :originId) " +
            "AND (:destinationId IS NULL OR c.destination.cityId = :destinationId) ")
    List<Container> searchContainer(
            @Param("originId") Long originId,
            @Param("destinationId") Long destinationId
    );

}

