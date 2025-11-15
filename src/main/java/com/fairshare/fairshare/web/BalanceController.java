package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.Balance;
import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.repo.BalanceRepository;
import com.fairshare.fairshare.repo.GroupRepository;
import com.fairshare.fairshare.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BalanceController {

    private final BalanceRepository balanceRepository;
    private final BalanceService balanceService;
    private final GroupRepository groupRepository;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Balance>> getGroupBalances(@PathVariable Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return ResponseEntity.ok(balanceRepository.findByGroup(group));
    }

    @GetMapping("/simplify/{groupId}")
    public ResponseEntity<List<String>> simplifyGroup(@PathVariable Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<String> transactions = balanceService.simplifyDebts(group);
        return ResponseEntity.ok(transactions);
    }
}