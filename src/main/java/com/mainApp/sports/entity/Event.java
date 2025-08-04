package com.mainApp.sports.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /** "indoor" or "outdoor" */
    @Column(nullable=false)
    private String type;

    @Column(nullable=false)
    private String nameOfEvent;

    /** newly rated skill by player */
    private Integer rating;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="player_id")
    private Player player;

    // getters/setters, constructors...
}
