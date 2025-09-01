package com.capstone.cargo.repository;

import com.capstone.cargo.model.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {

    @Query(value = "SELECT EVENT_TYPE FROM TRACKING_EVENT WHERE CONTAINER_ID = :containerId ORDER BY EVENT_DATE DESC LIMIT 1", nativeQuery = true)
    String findByLatestContainerEvent(@Param("containerId") Long containerId);
}
