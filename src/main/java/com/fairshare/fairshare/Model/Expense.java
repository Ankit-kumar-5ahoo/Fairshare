package com.fairshare.fairshare.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "paid_by_user_id")
    private User paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private LocalDateTime createdAt = LocalDateTime.now();
}
