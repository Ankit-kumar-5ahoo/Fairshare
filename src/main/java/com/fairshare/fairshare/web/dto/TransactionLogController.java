package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.TransactionLog;
import com.fairshare.fairshare.repo.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionLogController {

    private final TransactionLogRepository logRepository;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<TransactionLog>> getGroupLogs(@PathVariable Long groupId) {
        List<TransactionLog> logs = logRepository.findByGroupIdOrderByTimestampDesc(groupId);
        return ResponseEntity.ok(logs);
    }
}