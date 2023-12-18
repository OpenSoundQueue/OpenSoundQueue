package com.example.backend.Repository;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "UserInfo")
public class UserInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "email")
    private String email;

    @Column(name = "verified")
    private boolean verified;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    private Collection<Role> roles;

    @Column
    @DateTimeFormat
    private Date lastOnline;

    public UserInfoEntity() {

    }

    public UserInfoEntity(String username, String password, String email, boolean verified) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.verified = verified;
    }

    public UserInfoEntity(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isVerified() {
        return verified;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", verified='" + verified + '\'' +
                ", lastOnline=" + lastOnline +
                ", roles=" + getRoles() +
                '}';
    }
}
