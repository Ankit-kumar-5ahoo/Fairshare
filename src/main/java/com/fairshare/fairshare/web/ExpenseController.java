package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.Expense;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.Model.ExpenseDTO;
import com.fairshare.fairshare.repo.UserRepository;
import com.fairshare.fairshare.service.ExpenseService;
import com.fairshare.fairshare.web.dto.CreateExpenseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody @Valid CreateExpenseRequest req,
                                                    @AuthenticationPrincipal UserDetails principal) {

        User actor = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseDTO savedDTO = expenseService.addExpense(req);
        return ResponseEntity.ok(savedDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id,
                                                    @RequestBody @Valid CreateExpenseRequest updatedExpense,
                                                    @AuthenticationPrincipal UserDetails principal) {

        userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        ExpenseDTO savedDTO = expenseService.updateExpense(id, updatedExpense);
        return ResponseEntity.ok(savedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails principal) {

        userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByGroup(@PathVariable Long groupId) {

        return ResponseEntity.ok(expenseService.getExpensesByGroup(groupId));
    }
}