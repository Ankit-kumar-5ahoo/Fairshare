package com.fairshare.fairshare.web.dto;

import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.GroupMember;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.GroupMemberRepository;
import com.fairshare.fairshare.repo.GroupRepository;
import com.fairshare.fairshare.repo.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupRepository groupRepo;
    private final UserRepository userRepo;
    private final GroupMemberRepository gmRepo;

    public GroupController(GroupRepository groupRepo, UserRepository userRepo, GroupMemberRepository gmRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.gmRepo = gmRepo;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Group> create(@RequestBody @Valid CreateGroupRequest req)
    {
        Group g = groupRepo.save(new Group(req.getName()));
        for (Long uid : req.getMemberUserIds())
        {
            User u = userRepo.findById(uid).orElseThrow();
            gmRepo.save(new GroupMember(g, u));
        }
        return ResponseEntity.created(URI.create("/api/groups/" + g.getId())).body(g);
    }

    @GetMapping("/{id}/members")
    public List<GroupMember> members(@PathVariable Long id)
    {
        return gmRepo.findByGroupId(id);
    }
    @PostMapping("/create-by-email")
    @Transactional
    public ResponseEntity<String> createGroupByEmail(@RequestBody CreateGroupByEmailRequest request) {


        Group group = new Group(request.getName());
        groupRepo.save(group);


        for (String email : request.getMemberEmails()) {

            User user = userRepo.findByEmail(email).orElse(null);

            if (user == null) {
                // Create a user with default name (before '@')
                String defaultName = email.split("@")[0];
                user = new User(defaultName, email);
                userRepo.save(user);
            }

            // 3️⃣ Add user to the group
            if (!gmRepo.existsByGroupIdAndUserId(group.getId(), user.getId()))
            {
                gmRepo.save(new GroupMember(group, user));
            }
        }

        // 4️⃣ Return a simple response
        String message = "Group '" + group.getName() + "' created with members: " + request.getMemberEmails();
        return ResponseEntity.ok(message);
    }

}
