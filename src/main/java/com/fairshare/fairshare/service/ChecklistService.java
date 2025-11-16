package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.ChecklistItem;
import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.Model.ChecklistItemDTO;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

import com.fairshare.fairshare.repo.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;

    private ChecklistItemDTO convertToDTO(ChecklistItem item) {
        ChecklistItemDTO dto = new ChecklistItemDTO();
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setCompleted(item.isCompleted());
        return dto;
    }

    public List<ChecklistItemDTO> getUserChecklist(User user) {
        return checklistRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public ChecklistItemDTO addItem(User user, String description) {
        ChecklistItem item = ChecklistItem.builder()
                .description(description)
                .user(user)
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();
        ChecklistItem saved = checklistRepository.save(item);
        return convertToDTO(saved);
    }


    @Transactional
    public ChecklistItemDTO toggleCompleted(Long itemId, User user) {
        ChecklistItem item = checklistRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        item.setCompleted(!item.isCompleted());
        ChecklistItem saved = checklistRepository.save(item);
        return convertToDTO(saved);
    }

    @Transactional
    public ChecklistItemDTO updateItem(Long itemId, String newDescription, User user) {
        ChecklistItem item = checklistRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        item.setDescription(newDescription);
        ChecklistItem saved = checklistRepository.save(item);
        return convertToDTO(saved);
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