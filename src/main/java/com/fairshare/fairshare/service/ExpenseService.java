package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.Expense;
import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.ExpenseRepository;
import com.fairshare.fairshare.repo.GroupRepository;
import com.fairshare.fairshare.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BalanceService balanceService;

    public Expense createExpense(Expense expense) {
        Group group = groupRepository.findById(expense.getGroup().getId())
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User payer = userRepository.findById(expense.getPaidBy().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        expense.setGroup(group);
        expense.setPaidBy(payer);

        Expense saved = expenseRepository.save(expense);

        balanceService.updateBalancesAfterExpense(group, payer, expense.getAmount());
        return saved;
    }

    public List<Expense> getExpensesByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return expenseRepository.findByGroup(group);
    }

    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found");
        }
        expenseRepository.deleteById(id);
    }
}