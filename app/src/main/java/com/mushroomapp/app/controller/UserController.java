package com.mushroomapp.app.controller;

import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final HttpRequestReader httpRequestReader = new FirebaseRequestReader();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            this.userService.save(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        try {
            this.userService.deleteById(id);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable UUID id) {
        try {
            User user = this.userService.getUserById(id);
            if(user == null) throw new IllegalArgumentException("user not found with id " + id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<User> users = this.userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/follow/{id}")
    public ResponseEntity<Object> userFollowsUser(@PathVariable UUID id, HttpServletRequest request) {
        try {
            String token = httpRequestReader.getId(request);

            User currentUser = this.userService.getUserByToken(token);
            User userToFollow = this.userService.getUserById(id);

            userService.followUser(currentUser, userToFollow);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
