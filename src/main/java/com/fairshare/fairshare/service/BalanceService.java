package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.Balance;
import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.GroupMember;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.BalanceRepository;
import com.fairshare.fairshare.repo.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final GroupMemberRepository groupMemberRepository;

    public void updateBalancesAfterExpense(Group group, User payer, double totalAmount) {
        List<GroupMember> members = groupMemberRepository.findByGroup(group);
        int totalMembers = members.size();
        double share = totalAmount / totalMembers;

        for (GroupMember member : members) {
            User current = member.getUser();

            if (current.getId().equals(payer.getId())) continue;

            Optional<Balance> existingBalanceOpt =
                    balanceRepository.findByGroupAndFromUserAndToUser(group, current, payer);

            if (existingBalanceOpt.isPresent()) {
                Balance existing = existingBalanceOpt.get();
                existing.setAmount(existing.getAmount() + share);
                balanceRepository.save(existing);
            } else {
                Balance newBalance = Balance.builder()
                        .group(group)
                        .fromUser(current)
                        .toUser(payer)
                        .amount(share)
                        .build();
                balanceRepository.save(newBalance);
            }
        }
    }

    public List<String> simplifyDebts(Group group) {
        List<Balance> balances = balanceRepository.findByGroup(group);
        Map<User, Double> netBalance = new HashMap<>();

        for (Balance b : balances) {
            netBalance.put(b.getFromUser(),
                    netBalance.getOrDefault(b.getFromUser(), 0.0) - b.getAmount());
            netBalance.put(b.getToUser(),
                    netBalance.getOrDefault(b.getToUser(), 0.0) + b.getAmount());
        }

        List<Map.Entry<User, Double>> entries = new ArrayList<>(netBalance.entrySet());
        entries.sort(Map.Entry.comparingByValue());

        int left = 0;
        int right = entries.size() - 1;
        List<String> transactions = new ArrayList<>();

        while (left < right) {
            User debtor = entries.get(left).getKey();
            User creditor = entries.get(right).getKey();

            double debit = -entries.get(left).getValue();
            double credit = entries.get(right).getValue();
            double settled = Math.min(debit, credit);

            if (settled > 0) {
                transactions.add(debtor.getName() + " pays " +
                        String.format("%.2f", settled) + " to " + creditor.getName());

                entries.get(left).setValue(entries.get(left).getValue() + settled);
                entries.get(right).setValue(entries.get(right).getValue() - settled);
            }

            if (entries.get(left).getValue() >= 0) left++;
            if (entries.get(right).getValue() <= 0) right--;
        }

        return transactions;
    }
}
