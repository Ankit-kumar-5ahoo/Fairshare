package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.Expense;
import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.repo.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense newExpense) {
        return expenseRepository.findById(id)
                .map(expense -> {
                    expense.setDescription(newExpense.getDescription());
                    expense.setAmount(newExpense.getAmount());
                    expense.setPaidBy(newExpense.getPaidBy());
                    expense.setGroup(newExpense.getGroup());
                    return expenseRepository.save(expense);
                })
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> getExpensesByGroup(Group group) {
        return expenseRepository.findByGroup(group);
    }
}