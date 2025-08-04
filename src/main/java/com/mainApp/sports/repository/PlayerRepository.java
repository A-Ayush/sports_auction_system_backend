package com.mainApp.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mainApp.sports.entity.Player;


public interface PlayerRepository extends JpaRepository<Player, Long> {
}

