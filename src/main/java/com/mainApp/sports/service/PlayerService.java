package com.mainApp.sports.service;

import com.mainApp.sports.entity.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerService {
    List<Player> listAll();
    Optional<Player> getById(Long id);
    Player create(Player player);
    Player update(Long id, Player changes);
    void delete(Long id);
    Optional<Player> findByEmpIdAndName(String empId, String name);
    boolean hasSubmittedEvents(Long id);
}
