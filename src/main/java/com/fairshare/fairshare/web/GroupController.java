package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.GroupMember;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.UserRepository;
import com.fairshare.fairshare.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(
            @RequestParam String name,
            @RequestBody List<String> memberEmails,
            @AuthenticationPrincipal UserDetails principal) {

        User creator = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group created = groupService.createGroupByEmail(name, memberEmails, creator);
        return ResponseEntity.created(URI.create("/api/groups/" + created.getId())).body(created);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> getGroupMembers(@PathVariable Long groupId) {
        List<GroupMember> members = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }

    @PutMapping("/{groupId}/rename")
    public ResponseEntity<Group> renameGroup(
            @PathVariable Long groupId,
            @RequestParam String newName,
            @AuthenticationPrincipal UserDetails principal) {

        User actor = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group updated = groupService.renameGroup(groupId, newName, actor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetails principal) {

        User actor = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        groupService.deleteGroup(groupId, actor);
        return ResponseEntity.ok("Group deleted successfully.");
    }

    @GetMapping("/my-groups")
    public ResponseEntity<List<Group>> getUserGroups(@AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Group> groups = groupService.getUserGroups(user);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<String> joinGroupByCode(
            @PathVariable String inviteCode,
            @AuthenticationPrincipal UserDetails principal) {

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return groupService.findByInviteCode(inviteCode)
                .map(group -> {
                    // Check if already a member
                    boolean alreadyMember = group.getMembers().stream()
                            .anyMatch(m -> m.getUser().getId().equals(user.getId()));

                    if (alreadyMember) {
                        return ResponseEntity.badRequest().body("User already in the group.");
                    }

                    // Add as member
                    group.getMembers().add(new com.fairshare.fairshare.Model.GroupMember(null, group, user));
                    return ResponseEntity.ok("Joined group: " + group.getName());
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Invalid invite code."));
    }
}
