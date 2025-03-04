package com.example.springoauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @NotBlank
    @Column(name = "github_id")
    String githubId;

    @NotBlank
    @Column(name = "name")
    String name;

    @NotBlank
    @Column(name = "email")
    String email;

    @NotBlank
    @Column(name = "role")
    String role;

    @Transient
    Map<String, Object> attributes;

    public User() {
    }

    public User(Long userId, String githubId, String name, String email, String role) {
        this.userId = userId;
        this.githubId = githubId;
        this.name = name;
        this.email = email;
        this.role = role;

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Map<String, Object> getAttributes() {
        return attributes != null ? attributes : Map.of();
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    // Методы OAuth2User
    @Override
    public Collection<? extends SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }
}
