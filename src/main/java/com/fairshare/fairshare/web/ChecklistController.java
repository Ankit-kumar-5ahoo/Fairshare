package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.ChecklistItem;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.Model.ChecklistItemDTO;
import com.fairshare.fairshare.repo.UserRepository;
import com.fairshare.fairshare.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checklist")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChecklistController {

    private final ChecklistService checklistService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ChecklistItemDTO>> getChecklist(
            @AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(checklistService.getUserChecklist(user));
    }

    @PostMapping
    public ResponseEntity<ChecklistItemDTO> addItem(
            @RequestBody Map<String, String> req,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String description = req.get("description");

        ChecklistItemDTO saved = checklistService.addItem(user, description);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ChecklistItemDTO> updateItem(
            @PathVariable Long id,
            @RequestBody Map<String, String> req,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String description = req.get("description");

        ChecklistItemDTO updated = checklistService.updateItem(id, description, user);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ChecklistItemDTO> toggleItem(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ChecklistItemDTO updated = checklistService.toggleCompleted(id, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        checklistService.deleteItem(id, user);
        return ResponseEntity.noContent().build();
    }
}