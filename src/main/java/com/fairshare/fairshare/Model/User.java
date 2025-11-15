package com.fairshare.fairshare.Model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ROLE_USER";


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-memberships")
    private List<GroupMember> memberships;


    @OneToMany(mappedBy = "paidBy")
    @JsonManagedReference(value = "expense-paidby")
    private List<Expense> expenses;


    @OneToMany(mappedBy = "actor")
    @JsonManagedReference(value = "user-logs")
    private List<TransactionLog> logs;


    @OneToMany(mappedBy = "fromUser")
    @JsonManagedReference(value = "user-balance-from")
    private List<Balance> balancesOwed;


    @OneToMany(mappedBy = "toUser")
    @JsonManagedReference(value = "user-balance-to")
    private List<Balance> balancesReceivable;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-checklist")
    private List<ChecklistItem> checklist;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.password = "default";
        this.role = "ROLE_USER";
    }
}