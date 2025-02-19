package com.example.springmvcwithjsonview.Repository;

import com.example.springmvcwithjsonview.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Modifying
    @Query("update UserEntity u set u.fullName=?2,u.email=?3 where u.userId=?1")
    Integer updateUser(Long userId, String fullName, String email);

    @Modifying
    @Query("delete UserEntity u where u.userId=?1")
    int deleteUserEntityByUserId(Long userId);
}
