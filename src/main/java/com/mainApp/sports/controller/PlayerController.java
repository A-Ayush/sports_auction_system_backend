package com.mainApp.sports.controller;

import com.mainApp.sports.entity.Player;
import com.mainApp.sports.service.PlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.UrlResource;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;
    private final Path uploadDir;

    public PlayerController(PlayerService playerService, @Value("${file.upload-dir}") String uploadFolder) {
        this.playerService = playerService;
        this.uploadDir = Paths.get(uploadFolder).toAbsolutePath();
        try { Files.createDirectories(uploadDir); } catch (Exception ignored) {}
    }

    @GetMapping
    public List<Player> listAll() {
        return playerService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getById(@PathVariable Long id) {
        return playerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Player> create(
            @RequestPart String email,
            @RequestPart String name,
            @RequestPart String empId,
            @RequestPart String department,
            @RequestPart(required = false) String jerseyName,
            @RequestPart(required = false) String gender,
            @RequestPart(required = false) String jerseyNumber,
            @RequestPart(required = false) String size,
            @RequestPart(required = false) String role,
            @RequestPart(required = false) MultipartFile photo
    ) throws Exception {

        Player p1 = playerService.findByEmail(email)
                .orElseGet(() -> playerService.findByEmpIdAndName(empId, name)
                        .orElse(null));
        if (p1 != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(p1); // Conflict if player already exists
        }
        Player p = new Player();
        p.setEmail(email);
        p.setName(name);
        p.setEmpId(empId);
        p.setDepartment(department);
        p.setJerseyName(jerseyName);
        p.setGender(gender);
        p.setJerseyNumber(Integer.valueOf(jerseyNumber));
        p.setSize(size);
        p.setRole(role);

        if (photo != null && !photo.isEmpty()) {
            String orig = Paths.get(photo.getOriginalFilename()).getFileName().toString();
            String stored = UUID.randomUUID() + "-" + orig;
            Files.copy(photo.getInputStream(), uploadDir.resolve(stored), StandardCopyOption.REPLACE_EXISTING);
            p.setPhotoFilename(stored);
        }

        Player saved = playerService.create(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> update(
            @PathVariable Long id,
            @RequestBody Player changes
    ) {
        try {
            Player updated = playerService.update(id, changes);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getdetails")
    public ResponseEntity<Player> getDetailsByEmail(@RequestParam String email) {

        Player player = playerService.findByEmail(email)
                .orElse(null);
        if (player == null) {
            return ResponseEntity.notFound().build();
        } else {
            Player p1 = new Player();
            // Optionally, you can return the player details directly
            p1.setEmail(player.getEmail());
            p1.setName(player.getName());
            p1.setEmpId(player.getEmpId());
            return ResponseEntity.status(HttpStatus.OK).body(p1);
        }

    }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteByEmail(@RequestParam String email) {
      try {
          // Find the player by email
          Player player = playerService.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("Player not found"));

          // Delete associated events first
          player.getEvents().clear();
          playerService.save(player);

          // Delete the player
          playerService.delete(player.getId());

          return ResponseEntity.noContent().build();
      } catch (RuntimeException ex) {
          return ResponseEntity.notFound().build();
      }
  }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/photo")
    public Optional<ResponseEntity<?>> getPhoto(@PathVariable Long id) {
        return playerService.getById(id).map(player -> {
            if (player.getPhotoFilename() == null) return ResponseEntity.notFound().build();
            Path file = uploadDir.resolve(player.getPhotoFilename());
            if (!Files.exists(file)) return ResponseEntity.notFound().build();
            try {
                UrlResource resource = new UrlResource(file.toUri());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // optionally detect original type
                        .body(resource);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        });
    }

    @GetMapping("/{id}/hasSelectedEvents")
    public ResponseEntity<Boolean> hasSelectedEvents(@PathVariable Long id) {
        boolean hasSubmitted = playerService.hasSubmittedEvents(id);
        return ResponseEntity.ok(hasSubmitted);
    }
}
