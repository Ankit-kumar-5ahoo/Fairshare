package com.fairshare.fairshare.repo;

import com.fairshare.fairshare.Model.Expense;
import com.fairshare.fairshare.Model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup(Group group);
}