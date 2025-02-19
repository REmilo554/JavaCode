package com.example.springmvcwithjsonview.Controller;


import com.example.springmvcwithjsonview.Entity.UserEntity;
import com.example.springmvcwithjsonview.Service.UserService;
import com.example.springmvcwithjsonview.View.UserDetails;
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

    @GetMapping("/{id}")
    @JsonView(UserDetails.class)
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<UserEntity> save(@Valid @RequestBody UserEntity userEntity) {
        return new ResponseEntity<>(userService.save(userEntity), HttpStatus.CREATED);
    }

    /**
     * http://localhost:8080/users/v1/update
     * {
     * "userId":1,
     * "fullName":"John Smith",
     * "email":"john.smith@example.com"
     * }
     */
    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateUser(@Valid @RequestBody UserEntity userEntity) {
        return new ResponseEntity<>(userService.update(userEntity), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
    }
}
