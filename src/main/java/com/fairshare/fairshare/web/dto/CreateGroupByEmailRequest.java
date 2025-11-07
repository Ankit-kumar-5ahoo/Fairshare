package com.fairshare.fairshare.web.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;


public class CreateGroupByEmailRequest {
    @NotBlank
    private String name;

    private List<@Email String> memberEmails;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getMemberEmails() { return memberEmails; }
    public void setMemberEmails(List<String> memberEmails) { this.memberEmails = memberEmails; }
}
