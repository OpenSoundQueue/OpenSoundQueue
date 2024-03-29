package com.example.backend.Repository;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @ElementCollection(targetClass = Permissions.class)
    @CollectionTable(name = "Role_Permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    private Collection<Permissions> permissions;

    @ManyToMany(targetEntity = UserInfoEntity.class, fetch = FetchType.EAGER)
    private Collection<UserInfoEntity> members;

    public Role() {
    }

    public Role(int id, String name, Collection<Permissions> permissions, Collection<UserInfoEntity> members) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Collection<UserInfoEntity> getMembers() {
        return members;
    }

    public void setMembers(Collection<UserInfoEntity> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
