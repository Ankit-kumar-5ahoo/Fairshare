package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.Expense;
import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.TransactionLog;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.exception.ExpenseNotFoundException;
import com.fairshare.fairshare.exception.GroupNotFoundException;
import com.fairshare.fairshare.exception.UserNotFoundException;
import com.fairshare.fairshare.repo.*;
import com.fairshare.fairshare.web.dto.CreateExpenseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BalanceService balanceService;
    private final TransactionLogRepository logRepository;

    @Transactional
    public Expense addExpense(CreateExpenseRequest req) {

        Group group = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        User payer = userRepository.findById(req.getPaidBy())
                .orElseThrow(() -> new UserNotFoundException("Payer not found"));

        Expense expense = Expense.builder()
                .description(req.getDescription())
                .amount(req.getAmount())
                .group(group)
                .paidBy(payer)
                .build();

        Expense saved = expenseRepository.save(expense);

        balanceService.updateBalancesAfterExpense(group, payer, req.getAmount());

        logRepository.save(TransactionLog.builder()
                .group(group)
                .actor(payer)
                .action("ADD_EXPENSE")
                .details(payer.getName() + " added " + req.getAmount() + " for " + req.getDescription())
                .build());

        return saved;
    }

    @Transactional
    public Expense updateExpense(Long id, CreateExpenseRequest updated) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

        balanceService.updateBalancesAfterExpense(
                existing.getGroup(),
                existing.getPaidBy(),
                -existing.getAmount()
        );

        existing.setDescription(updated.getDescription());
        existing.setAmount(updated.getAmount());

        User newPayer = userRepository.findById(updated.getPaidBy())
                .orElseThrow(() -> new UserNotFoundException("Payer not found"));
        existing.setPaidBy(newPayer);

        Expense saved = expenseRepository.save(existing);

        balanceService.updateBalancesAfterExpense(saved.getGroup(), newPayer, saved.getAmount());

        logRepository.save(TransactionLog.builder()
                .group(saved.getGroup())
                .actor(newPayer)
                .action("UPDATE_EXPENSE")
                .details("Updated expense to " + saved.getDescription() + " " + saved.getAmount())
                .build());

        return saved;
    }

    @Transactional
    public void deleteExpense(Long id) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

        balanceService.updateBalancesAfterExpense(existing.getGroup(), existing.getPaidBy(), -existing.getAmount());
        expenseRepository.delete(existing);

        logRepository.save(TransactionLog.builder()
                .group(existing.getGroup())
                .actor(existing.getPaidBy())
                .action("DELETE_EXPENSE")
                .details("Deleted expense: " + existing.getDescription() + " ₹" + existing.getAmount())
                .build());
    }

    public List<Expense> getExpensesByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));
        return expenseRepository.findByGroup(group);
    }
}
