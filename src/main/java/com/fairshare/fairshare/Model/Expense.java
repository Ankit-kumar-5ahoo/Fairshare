package com.fairshare.fairshare.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "paid_by_id", nullable = false)
    private User paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}