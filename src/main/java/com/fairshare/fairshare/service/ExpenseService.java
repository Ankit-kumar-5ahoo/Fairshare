package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.*;
import com.fairshare.fairshare.exception.ExpenseNotFoundException;
import com.fairshare.fairshare.exception.GroupNotFoundException;
import com.fairshare.fairshare.exception.UserNotFoundException;
import com.fairshare.fairshare.repo.*;
import com.fairshare.fairshare.web.dto.CreateExpenseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import com.fairshare.fairshare.Model.ExpenseDTO;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final BalanceService balanceService;
    private final TransactionLogRepository logRepository;

    @Transactional
    // --- 1. CHANGE RETURN TYPE TO ExpenseDTO ---
    public ExpenseDTO addExpense(CreateExpenseRequest req) {
        if (req.getAmount() == null || req.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than 0");
        }
        Group group = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));
        User payer = userRepository.findById(req.getPaidBy())
                .orElseThrow(() -> new UserNotFoundException("Payer not found"));
        boolean isMember = groupMemberRepository.findByGroup(group).stream()
                .anyMatch(gm -> gm.getUser().getId().equals(payer.getId()));
        if (!isMember) {
            throw new IllegalStateException("Payer is not a member of this group");
        }
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


        return convertExpenseToDTO(saved);
    }

    @Transactional

    public ExpenseDTO updateExpense(Long id, CreateExpenseRequest updated) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        if (updated.getAmount() == null || updated.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than 0");
        }
        balanceService.updateBalancesAfterExpense(existing.getGroup(), existing.getPaidBy(), -existing.getAmount());
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
                .details("Updated expense: " + saved.getDescription() + " ₹" + saved.getAmount())
                .build());

        // --- 5. RETURN THE SAFE DTO ---
        return convertExpenseToDTO(saved);
    }

    @Transactional
    public void deleteExpense(Long id) {
        // (This method is fine)
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        balanceService.updateBalancesAfterExpense(existing.getGroup(), existing.getPaidBy(), -existing.getAmount());
        expenseRepository.delete(existing);
        logRepository.save(TransactionLog.builder()
                .group(existing.getGroup())
                .actor(existing.getPaidBy())
                .action("DELETE_EXPENSE")
                .details("Deleted expense: " + existing.getDescription() + " " + existing.getAmount())
                .build());
    }

    public List<ExpenseDTO> getExpensesByGroup(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));
        List<Expense> expenses = expenseRepository.findByGroup(group);
        return expenses.stream()
                .map(this::convertExpenseToDTO)
                .collect(Collectors.toList());
    }

    private ExpenseDTO convertExpenseToDTO(Expense expense) {
        UserDTO paidByDTO = new UserDTO();
        paidByDTO.setId(expense.getPaidBy().getId());
        paidByDTO.setName(expense.getPaidBy().getName());
        paidByDTO.setEmail(expense.getPaidBy().getEmail());

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setPaidBy(paidByDTO);

        return expenseDTO;
    }
}