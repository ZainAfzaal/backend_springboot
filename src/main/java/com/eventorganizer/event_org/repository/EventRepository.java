package com.eventorganizer.event_org.repository;

import com.eventorganizer.event_org.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.date) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")

    List<Event> searchEvents(String keyword);

    @Query("SELECT e FROM Event e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.date) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.createdBy) LIKE LOWER(CONCAT('%', :keyword, '%'))")

    List<Event> searchEventsByAdmin(String keyword);


}
