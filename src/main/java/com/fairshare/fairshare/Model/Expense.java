package com.fairshare.fairshare.Model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "expenses")
@Getter
@Setter
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
    private double amount;


    @ManyToOne(optional = false)
    @JoinColumn(name = "paid_by")
    @JsonBackReference(value = "user-expenses")
    private User paidBy;


    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    @JsonBackReference(value = "group-expenses")
    private Group group;
}