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

    @GetMapping
    public ResponseEntity<?> getDashboard(
            @AuthenticationPrincipal UserDetails principal
    ) {
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GroupMember> memberships = groupMemberRepository.findByUser(user);
        List<Group> userGroups = memberships.stream()
                .map(GroupMember::getGroup)
                .toList();

        List<Expense> recentExpenses = expenseRepository.findTop10ByPaidByOrderByIdDesc(user);

        List<Balance> balances = balanceRepository.findAllByUserInvolved(user.getId());

        Map<Long, List<String>> simplifiedSettlements = new HashMap<>();
        for (Group g : userGroups) {
            simplifiedSettlements.put(g.getId(), balanceService.simplifyDebts(g));
        }

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("user", user);
        dashboard.put("groups", userGroups);
        dashboard.put("recentExpenses", recentExpenses);
        dashboard.put("balances", balances);
        dashboard.put("settlements", simplifiedSettlements);

        return ResponseEntity.ok(dashboard);
    }
}
