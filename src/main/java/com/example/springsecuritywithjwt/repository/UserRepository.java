package com.example.springsecuritywithjwt.repository;

import com.example.springsecuritywithjwt.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NotBlank String username);

    void deleteByUsername(@NotBlank String username);

    @Query("SELECT u FROM User u where u.role='ADMIN'")
    List<User> getAllAdmin();

    @Query("SELECT u FROM User u where u.role='USER'")
    List<User> getAllUsers();
}
