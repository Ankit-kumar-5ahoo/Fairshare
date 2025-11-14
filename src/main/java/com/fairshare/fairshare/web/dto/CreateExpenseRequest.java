package com.fairshare.fairshare.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExpenseRequest {

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Group ID is required")
    private Long groupId;

    @NotNull(message = "Payer user ID is required")
    private Long paidBy;
}