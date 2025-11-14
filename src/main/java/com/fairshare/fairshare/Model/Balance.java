package com.fairshare.fairshare.Model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "from_user_id")
    @JsonBackReference(value = "user-balance-from")
    private User fromUser;


    @ManyToOne(optional = false)
    @JoinColumn(name = "to_user_id")
    @JsonBackReference(value = "user-balance-to")
    private User toUser;


    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    @JsonBackReference(value = "group-balances")
    private Group group;

    @Column(nullable = false)
    private double amount;
}