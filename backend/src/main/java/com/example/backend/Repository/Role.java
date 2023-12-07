package com.example.backend.Repository;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToMany(targetEntity = UserInfoEntity.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<UserInfoEntity> usersList;

    public Role() {
    }

    public Role(int id, String name, Collection<Permissions> permissions, Collection<UserInfoEntity> usersList) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.usersList = usersList;
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

    public Collection<UserInfoEntity> getUsersList() {
        return usersList;
    }

    public void setUsersList(Collection<UserInfoEntity> usersList) {
        this.usersList = usersList;
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
