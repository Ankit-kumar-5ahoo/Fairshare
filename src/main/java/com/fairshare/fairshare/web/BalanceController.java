package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.Balance;
import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.BalanceDTO;
import java.util.stream.Collectors;
import com.fairshare.fairshare.repo.BalanceRepository;
import com.fairshare.fairshare.repo.GroupRepository;
import com.fairshare.fairshare.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// --- THIS IS THE FIXED LINE ---
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BalanceController {

    private final BalanceRepository balanceRepository;
    private final BalanceService balanceService;
    private final GroupRepository groupRepository;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<BalanceDTO>> getGroupBalances(@PathVariable Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Balance> balances = balanceRepository.findByGroup(group);


        List<BalanceDTO> balanceDTOs = balances.stream().map(balance -> {
            BalanceDTO dto = new BalanceDTO();
            dto.setFromUser(balance.getFromUser().getName());
            dto.setToUser(balance.getToUser().getName());
            dto.setAmount(balance.getAmount());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(balanceDTOs);
    }

    @GetMapping("/simplify/{groupId}")
    public ResponseEntity<List<String>> simplifyGroup(@PathVariable Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<String> transactions = balanceService.simplifyDebts(group);
        return ResponseEntity.ok(transactions);
    }
}