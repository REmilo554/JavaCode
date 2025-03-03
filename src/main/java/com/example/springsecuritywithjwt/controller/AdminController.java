package com.example.springsecuritywithjwt.controller;

import com.example.springsecuritywithjwt.entity.User;
import com.example.springsecuritywithjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * http://localhost:8081/admin/getuser?username=user1
     */
    @GetMapping("/getuser")
    public ResponseEntity<?> checkUser(@RequestParam String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }


    @GetMapping("/getallusers")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<Void> deleteUser(@RequestBody String username) {
        userService.deleteUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/unlock/{username}")
    public ResponseEntity<?> unlockUserAccount(@PathVariable String username) {
        String result = userService.unlockAccount(username);

        if (result.equals("User account unlocked")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
