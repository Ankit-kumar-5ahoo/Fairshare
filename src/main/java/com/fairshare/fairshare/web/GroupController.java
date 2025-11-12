package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.GroupMember;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.GroupMemberRepository;
import com.fairshare.fairshare.repo.GroupRepository;
import com.fairshare.fairshare.repo.UserRepository;
import com.fairshare.fairshare.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository gmRepo;

    @PostMapping("/create-by-email")
    public ResponseEntity<Map<String, Object>> createGroupByEmail(
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal UserDetails principal) {

        String name = (String) payload.get("name");
        List<String> memberEmails = (List<String>) payload.get("memberEmails");

        User creator = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group saved = groupService.createGroupByEmail(name, memberEmails, creator);

        return ResponseEntity.ok(Map.of(
                "message", "Group '" + saved.getName() + "' created successfully",
                "groupId", saved.getId()
        ));
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> getMembers(@PathVariable Long groupId) {
        List<GroupMember> members = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }

    @PutMapping("/{groupId}/rename")
    public ResponseEntity<Map<String, String>> renameGroup(
            @PathVariable Long groupId,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails principal) {

        String newName = payload.get("name");
        User actor = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group updated = groupService.renameGroup(groupId, newName, actor);
        return ResponseEntity.ok(Map.of("message", "Group renamed to " + updated.getName()));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Map<String, String>> deleteGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetails principal) {

        User actor = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        groupService.deleteGroup(groupId, actor);
        return ResponseEntity.ok(Map.of("message", "Group deleted successfully"));
    }
}
