package com.example.springmvcwithjsonview.Controller;


import com.example.springmvcwithjsonview.Entity.UserEntity;
import com.example.springmvcwithjsonview.Service.UserService;
import com.example.springmvcwithjsonview.View.UserSummary;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/v1")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @JsonView(UserSummary.class)
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    @JsonView(UserSummary.class)
    public ResponseEntity<UserEntity> getUser(@PathVariable UUID user_id) {
        System.out.println(user_id);

        return new ResponseEntity<>(userService.findById(user_id),HttpStatus.OK);
    }

    @PostMapping("/add/")
    public UserEntity save(@Valid @RequestBody UserEntity userEntity) {
        return userService.save(userEntity);
    }

    @PutMapping("/update")
    public UserEntity updateUser(@Valid @RequestBody UserEntity userEntity) {
        return userService.update(userEntity);
    }

    @DeleteMapping("/delete/{user_uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID user_uuid) {
       userService.delete(user_uuid);
       return new ResponseEntity<>(HttpStatus.OK);
    }
}
