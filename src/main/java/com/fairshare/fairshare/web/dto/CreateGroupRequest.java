package com.fairshare.fairshare.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CreateGroupRequest {
    @NotBlank private String name;
    @NotNull  private List<Long> memberUserIds;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Long> getMemberUserIds() { return memberUserIds; }
    public void setMemberUserIds(List<Long> memberUserIds) { this.memberUserIds = memberUserIds; }
}
