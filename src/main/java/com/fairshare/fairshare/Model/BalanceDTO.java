package com.fairshare.fairshare.Model;

import lombok.Data;


@Data
public class BalanceDTO {
    private String fromUser;
    private String toUser;
    private double amount;
}