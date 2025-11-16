package com.fairshare.fairshare.repo;

import com.fairshare.fairshare.Model.Group;
import com.fairshare.fairshare.Model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByGroupIdOrderByTimestampDesc(Long groupId);


    List<TransactionLog> findTop10ByGroupInOrderByTimestampDesc(List<Group> groups);
}