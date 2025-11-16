package com.fairshare.fairshare.Model;

import lombok.Data;


@Data
public class ChecklistItemDTO {
    private Long id;
    private String description;
    private boolean completed;
}