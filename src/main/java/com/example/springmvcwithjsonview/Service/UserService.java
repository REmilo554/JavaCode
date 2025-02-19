package com.example.springmvcwithjsonview.Service;

import com.example.springmvcwithjsonview.Entity.UserEntity;
import com.example.springmvcwithjsonview.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        Optional<UserEntity> user = userRepository.findUserEntityByUserId(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
        return user.get();
    }

    public UserEntity update(UserEntity userEntity) {
        return userRepository.updateUser(userEntity.getUserId(), userEntity.getFullName(), userEntity.getEmail());
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }


    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
