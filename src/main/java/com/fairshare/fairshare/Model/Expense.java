package com.fairshare.fairshare.Model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "expenses")
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
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore
    private Group group;

    @ManyToOne
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;
}