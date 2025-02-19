package com.example.springmvcwithjsonview.Repository;

import com.example.springmvcwithjsonview.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

//    @Query("select '*' from UserEntity u join Order o on u.userId=o.user where o.user=?1")
   Optional<UserEntity> findUserEntityByUserId(UUID userId);

    @Modifying
    @Query("update UserEntity u set u.fullName=:fullName,u.email=:email where u.userId=:userId")
    UserEntity updateUser(UUID userId,String fullName,String email);
}
