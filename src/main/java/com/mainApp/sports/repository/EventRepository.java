package com.mainApp.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mainApp.sports.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
