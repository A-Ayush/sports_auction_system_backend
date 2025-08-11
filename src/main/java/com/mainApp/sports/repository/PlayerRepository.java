package com.mainApp.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mainApp.sports.entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByEmpIdAndName(String empId, String name);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Event e WHERE e.player.id = :playerId")
    boolean hasSubmittedEvents(@Param("playerId") Long playerId);


}

