package com.mainApp.sports.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false)
    private String email;

    @Column(name = "emp_id", nullable = false)
    private String empId;

    private String department;

    /** filename of uploaded profile picture */
    private String photoFilename;

    // 🆕 Jersey details
    @Column(name = "jersey_name")
    private String jerseyName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    @Column(name = "size")
    private String size;

    // 🆕 Role selection
    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE) // manage list yourself for bi-directional safety
    @JsonIgnore
    private List<Event> events = new ArrayList<>();
}
