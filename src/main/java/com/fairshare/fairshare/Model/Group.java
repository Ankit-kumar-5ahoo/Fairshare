package com.fairshare.fairshare.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Group() {}
    public Group(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
