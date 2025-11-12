package com.fairshare.fairshare.repo;

import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.GroupMember;
import com.fairshare.fairshare.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByGroup(Group group);
    List<GroupMember> findByGroupId(Long groupId);

    boolean existsByGroupAndUser(Group group, User user);
}
