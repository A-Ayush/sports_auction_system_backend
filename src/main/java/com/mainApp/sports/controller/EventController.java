package com.mainApp.sports.controller;

import com.mainApp.sports.entity.Event;
import com.mainApp.sports.repository.EventRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository repo;
    public EventController(EventRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Event> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Event> get(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Event create(@RequestBody Event e) {
        return repo.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable Long id, @RequestBody Event e) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setType(e.getType());
                    existing.setNameOfEvent(e.getNameOfEvent());
                    existing.setRating(e.getRating());
                    // optionally conditionally update player
                    return repo.save(existing);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
