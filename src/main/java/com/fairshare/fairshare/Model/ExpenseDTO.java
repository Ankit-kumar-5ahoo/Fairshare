package com.fairshare.fairshare.Model;

import lombok.Data;


@Data
public class ExpenseDTO {
    private Long id;
    private String description;
    private double amount;
    private UserDTO paidBy; // This holds { id, name, email }
}