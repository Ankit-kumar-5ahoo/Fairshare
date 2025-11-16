package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.*;
import com.fairshare.fairshare.repo.*;
import com.fairshare.fairshare.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


import com.fairshare.fairshare.Model.UserDTO;
import com.fairshare.fairshare.Model.GroupDTO;
import com.fairshare.fairshare.Model.ExpenseDTO;
import com.fairshare.fairshare.Model.BalanceDTO;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ExpenseRepository expenseRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceService balanceService;
    private final TransactionLogRepository transactionLogRepository; // <-- 1. ADD THIS

    @GetMapping("/me")
    public ResponseEntity<?> getDashboard(
            @AuthenticationPrincipal UserDetails principal
    ) {
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());


        List<GroupMember> memberships = groupMemberRepository.findByUser(user);
        List<Group> userGroups = memberships.stream()
                .map(GroupMember::getGroup)
                .toList();
        List<GroupDTO> groupDTOs = userGroups.stream().map(group -> {
            GroupDTO dto = new GroupDTO();
            dto.setId(group.getId());
            dto.setName(group.getName());
            return dto;
        }).collect(Collectors.toList());


        List<Expense> recentExpenses = expenseRepository.findTop10ByPaidByOrderByIdDesc(user);
        List<ExpenseDTO> expenseDTOs = recentExpenses.stream().map(expense -> {
            ExpenseDTO dto = new ExpenseDTO();
            dto.setId(expense.getId());
            dto.setDescription(expense.getDescription());
            dto.setAmount(expense.getAmount());
            return dto;
        }).collect(Collectors.toList());


        List<Balance> balances = balanceRepository.findAllByUserInvolved(user.getId());
        List<BalanceDTO> balanceDTOs = balances.stream().map(balance -> {
            BalanceDTO dto = new BalanceDTO();
            dto.setFromUser(balance.getFromUser().getName());
            dto.setToUser(balance.getToUser().getName());
            dto.setAmount(balance.getAmount());
            return dto;
        }).collect(Collectors.toList());
        List<String> recentLogs = new ArrayList<>();
        if (!userGroups.isEmpty()) {
            List<TransactionLog> logs = transactionLogRepository.findTop10ByGroupInOrderByTimestampDesc(userGroups);
            recentLogs = logs.stream()
                    .map(TransactionLog::getDetails)
                    .collect(Collectors.toList());
        }

        Map<Long, List<String>> simplifiedSettlements = new HashMap<>();
        for (Group g : userGroups) {
            simplifiedSettlements.put(g.getId(), balanceService.simplifyDebts(g));
        }

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("user", userDTO);
        dashboard.put("groups", groupDTOs);
        dashboard.put("recentExpenses", expenseDTOs);
        dashboard.put("balances", balanceDTOs);
        dashboard.put("settlements", simplifiedSettlements);
        dashboard.put("recentLogs", recentLogs);

        return ResponseEntity.ok(dashboard);
    }
}