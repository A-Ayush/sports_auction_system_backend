package com.mainApp.sports.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "emp_id", nullable = false)
    private String empId;

    private String department;

    /** filename of uploaded profile picture */
    private String photoFilename;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE) // manage list yourself for bi-directional safety
    private List<Event> events = new ArrayList<>();

    // equals()/hashCode() manually if needed, but not auto-generated
}
