package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.GroupMember;
import com.fairshare.fairshare.Model.TransactionLog;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.GroupMemberRepository;
import com.fairshare.fairshare.repo.GroupRepository;
import com.fairshare.fairshare.repo.TransactionLogRepository;
import com.fairshare.fairshare.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public Group createGroupByEmail(String name, List<String> memberEmails, User creator) {

        String inviteCode = UUID.randomUUID().toString().substring(0, 8);

        Group group = Group.builder()
                .name(name)
                .createdBy(creator)
                .inviteCode(inviteCode)
                .build();

        Group savedGroup = groupRepository.save(group);

        groupMemberRepository.save(GroupMember.builder()
                .group(savedGroup)
                .user(creator)
                .build());

        for (String email : memberEmails) {
            userRepository.findByEmail(email).ifPresent(user -> {
                if (!groupMemberRepository.existsByGroupAndUser(savedGroup, user)) {
                    groupMemberRepository.save(GroupMember.builder()
                            .group(savedGroup)
                            .user(user)
                            .build());
                }
            });
        }

        transactionLogRepository.save(TransactionLog.builder()
                .group(savedGroup)
                .actor(creator)
                .action("CREATE_GROUP")
                .details(creator.getName() + " created group '" + name + "' with members: "
                        + String.join(", ", memberEmails))
                .build());

        return savedGroup;
    }

    public List<GroupMember> getGroupMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupMemberRepository.findByGroup(group);
    }

    @Transactional
    public Group renameGroup(Long groupId, String newName, User actor) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getCreatedBy().getId().equals(actor.getId())) {
            throw new RuntimeException("Only group creator can rename the group");
        }

        String oldName = group.getName();
        group.setName(newName);
        Group updated = groupRepository.save(group);

        transactionLogRepository.save(TransactionLog.builder()
                .group(group)
                .actor(actor)
                .action("RENAME_GROUP")
                .details(actor.getName() + " renamed group from '" + oldName + "' to '" + newName + "'")
                .build());

        return updated;
    }

    @Transactional
    public void deleteGroup(Long groupId, User actor) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getCreatedBy().getId().equals(actor.getId())) {
            throw new RuntimeException("Only group creator can delete the group");
        }

        transactionLogRepository.save(TransactionLog.builder()
                .group(group)
                .actor(actor)
                .action("DELETE_GROUP")
                .details(actor.getName() + " deleted group '" + group.getName() + "'")
                .build());

        groupRepository.delete(group);
    }

    public List<Group> getUserGroups(User user) {
        List<GroupMember> memberships = groupMemberRepository.findAll().stream()
                .filter(gm -> gm.getUser().getId().equals(user.getId()))
                .toList();
        return memberships.stream().map(GroupMember::getGroup).collect(Collectors.toList());
    }

    public Optional<Group> findByInviteCode(String code) {
        return groupRepository.findAll().stream()
                .filter(g -> g.getInviteCode().equals(code))
                .findFirst();
    }
}
