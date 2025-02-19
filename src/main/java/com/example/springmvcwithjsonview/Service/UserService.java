package com.example.springmvcwithjsonview.Service;

import com.example.springmvcwithjsonview.Entity.UserEntity;
import com.example.springmvcwithjsonview.ExceptionsHandler.UserNotFoundException;
import com.example.springmvcwithjsonview.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found", HttpStatus.NOT_FOUND);
        }
        return users;
    }

    @Transactional(readOnly = true)
    public UserEntity findById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("Id can't be null", HttpStatus.BAD_REQUEST);
        }
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        return user.get();
    }

    @Transactional
    public UserEntity update(UserEntity userEntity) {
        if (userEntity == null) {
            throw new UserNotFoundException("user can't be null", HttpStatus.BAD_REQUEST);
        }
        Integer countOfUpdates = userRepository
                .updateUser(userEntity.getUserId(), userEntity.getFullName(), userEntity.getEmail());
        if (countOfUpdates == null || countOfUpdates == 0) {
            throw new UserNotFoundException("user not found", HttpStatus.NOT_FOUND);
        }
        return userRepository.findById(userEntity.getUserId())
                .orElseThrow(() -> new UserNotFoundException("user not found after update", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public UserEntity save(UserEntity userEntity) {
        if (userEntity == null) {
            throw new UserNotFoundException("user can't be null", HttpStatus.BAD_REQUEST);
        }
        UserEntity save = userRepository.save(userEntity);
        if (save == null) {
            throw new UserNotFoundException("user not saved", HttpStatus.NOT_FOUND);
        }
        return save;
    }

    @Transactional
    public UserEntity delete(Long id) {
        if (id == null) {
            throw new UserNotFoundException("id can't be null", HttpStatus.BAD_REQUEST);
        }
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isEmpty()) {
            throw new UserNotFoundException("user not found", HttpStatus.NOT_FOUND);
        }
        int countOfDeletedRows = userRepository.deleteUserEntityByUserId(id);
        if (countOfDeletedRows == 0) {
            throw new UserNotFoundException("user not deleted", HttpStatus.NOT_FOUND);
        }
        return userEntity.get();
    }
}
