package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.ChecklistItem;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.UserRepository;
import com.fairshare.fairshare.service.ChecklistService;
import com.fairshare.fairshare.web.dto.ChecklistItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checklist")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChecklistController {

    private final ChecklistService checklistService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ChecklistItem>> getChecklist(
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(checklistService.getUserChecklist(user));
    }

    @PostMapping
    public ResponseEntity<ChecklistItem> addItem(
            @RequestBody ChecklistItemRequest req,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChecklistItem saved = checklistService.addItem(user, req.getDescription());
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChecklistItem> updateItem(
            @PathVariable Long id,
            @RequestBody ChecklistItemRequest req,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChecklistItem updated = checklistService.updateItem(id, req.getDescription(), user);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ChecklistItem> toggleItem(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChecklistItem updated = checklistService.toggleCompleted(id, user);
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
