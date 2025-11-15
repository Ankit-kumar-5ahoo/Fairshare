package com.fairshare.fairshare.repo;

import com.fairshare.fairshare.Model.ChecklistItem;
import com.fairshare.fairshare.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByUser(User user);
}
