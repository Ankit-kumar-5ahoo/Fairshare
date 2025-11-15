package com.fairshare.fairshare.web.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateGroupRequest {

    @NotBlank
    private String name;

    @NotNull
    private List<String> memberEmails;
}