package com.example.springmvcwithjsonview.Entity;


import com.example.springmvcwithjsonview.View.UserDetails;
import com.example.springmvcwithjsonview.View.UserSummary;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    @UuidGenerator
    @JsonView(UserSummary.class)
    UUID userId;

    @NotBlank
    @Column(name = "full_name", nullable = false)
    @JsonView(UserSummary.class)
    String fullName;

    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false)
    @JsonView(UserSummary.class)
    String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonView(UserDetails.class)
    List<Order> orders;
}
