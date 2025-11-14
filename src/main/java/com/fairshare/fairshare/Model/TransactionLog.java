package com.fairshare.fairshare.Model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    @JsonBackReference(value = "group-logs")
    private Group group;


    @ManyToOne(optional = false)
    @JoinColumn(name = "actor_id")
    @JsonBackReference(value = "user-logs")
    private User actor;


    @Column(nullable = false)
    private String action;

    @Column(nullable = false, length = 1000)
    private String details;


    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}