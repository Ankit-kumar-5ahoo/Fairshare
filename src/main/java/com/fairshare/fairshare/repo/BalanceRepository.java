package com.fairshare.fairshare.repo;

import com.fairshare.fairshare.Model.Balance;
import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    List<Balance> findByGroup(Group group);

    Optional<Balance> findByGroupAndFromUserAndToUser(Group group, User fromUser, User toUser);
}