package com.fairshare.fairshare.repo;

import com.fairshare.fairshare.Model.Expense;
import com.fairshare.fairshare.Model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup(Group group);
}
