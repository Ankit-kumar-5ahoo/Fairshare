package com.fairshare.fairshare.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "group_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "user_id"}))
public class GroupMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private User user;

    public GroupMember() {}
    public GroupMember(Group group, User user) { this.group = group; this.user = user; }

    public Long getId() { return id; }
    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
