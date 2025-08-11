package com.mainApp.sports.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mainApp.sports.repository.PlayerRepository;
import com.mainApp.sports.entity.Player;
import java.util.*;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository repo;

    public PlayerServiceImpl(PlayerRepository repo) { this.repo = repo; }

    @Override
    public List<Player> listAll() { return repo.findAll(); }

    @Override
    public Optional<Player> getById(Long id) { return repo.findById(id); }

    @Override
    public Player create(Player p) { return repo.save(p); }

    @Override
    public Player update(Long id, Player changes) {
        return repo.findById(id).map(existing -> {
            existing.setName(changes.getName());
            existing.setDepartment(changes.getDepartment());
            // empId should not change; skip photoFilename and events here
            return existing;
        }).map(repo::save).orElseThrow(() -> new RuntimeException("Player not found"));
    }

    @Override
    public void delete(Long id) { repo.deleteById(id); }

    @Override
    public Optional<Player> findByEmpIdAndName(String empId, String name) {
        return repo.findByEmpIdAndName(empId, name);
    }

    @Override
    public boolean hasSubmittedEvents(Long id){
        return repo.hasSubmittedEvents(id);
    }
}
