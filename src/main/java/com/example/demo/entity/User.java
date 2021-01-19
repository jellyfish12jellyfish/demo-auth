package com.example.demo.entity;
/*
 * Date: 12/2/20
 * Time: 6:01 PM
 * */


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20, message = "Username must have between 3 and 20 characters")
    @Column(name = "username")
    @Pattern(regexp = "^[a-zA-Z]([._](?![._])|[a-zA-Z0-9]){3,80}$", message = "Your username must start with a letter")
    private String username;

    @Size(min = 3, max = 25, message = "First name must have between 3 and 25 characters")
    @Column(name = "first_name", columnDefinition = "varchar default null")
    private String firstName;

    @Size(min = 3, max = 25, message = "Last name must have between 3 and 25 characters")
    @Column(name = "last_name", columnDefinition = "varchar default null")
    private String lastName;

    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 8, message = "At least 8 characters")
    @Column(name = "password")
    private String password;

    @Column(name = "created_at", columnDefinition = "timestamp default null")
    private Date createdAt;

    @Column(name = "last_login_at", columnDefinition = "timestamp default null")
    private Date lastLoginAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    @Transient // не будем сохранять в бд
    private String confirmPassword;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserQuestion> userQuestions = new HashSet<>();

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Set<UserQuestion> getUserQuestions() {
        return userQuestions;
    }

    public void setUserQuestions(Set<UserQuestion> userQuestions) {
        this.userQuestions = userQuestions;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
