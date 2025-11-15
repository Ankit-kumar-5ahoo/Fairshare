package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.ChecklistItem;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;

    public List<ChecklistItem> getUserChecklist(User user) {
        return checklistRepository.findByUser(user);
    }

    @Transactional
    public ChecklistItem addItem(User user, String description) {
        ChecklistItem item = ChecklistItem.builder()
                .description(description)
                .user(user)
                .build();
        return checklistRepository.save(item);
    }

    @Transactional
    public ChecklistItem toggleCompleted(Long itemId, User user) {
        ChecklistItem item = checklistRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        item.setCompleted(!item.isCompleted());
        return checklistRepository.save(item);
    }

    @Transactional
    public ChecklistItem updateItem(Long itemId, String newDescription, User user) {
        ChecklistItem item = checklistRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        item.setDescription(newDescription);
        return checklistRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long itemId, User user) {
        ChecklistItem item = checklistRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        checklistRepository.delete(item);
    }
}
