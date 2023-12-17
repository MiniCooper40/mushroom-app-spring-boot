package com.mushroomapp.app.controller;

import com.google.api.gax.rpc.NotFoundException;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public User createUser(@RequestBody User user) {
        try {
            this.userService.save(user);
            return user;
        } catch (Exception e) {
            return null;
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

        Optional<User> user = this.userService.getUserById(id);
        if (user.isPresent()) return ResponseEntity.ok(user.get());

        throw new NoSuchElementException("user not found with id " + id);
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
    public ResponseEntity<Object> userFollowsUser(@PathVariable UUID id, HttpServletRequest request) throws BadRequestException {

        String token = httpRequestReader.getId(request);

        Optional<User> currentUser = this.userService.getUserByToken(token);
        if (currentUser.isEmpty()) throw new NoSuchElementException("User does not exist with token " + token);

        Optional<User> userToFollow = this.userService.getUserById(id);
        if (userToFollow.isEmpty()) throw new BadRequestException("User does not exist with id " + id);

        userService.followUser(
                currentUser.get(),
                userToFollow.get()
        );

        return ResponseEntity.ok().build();

    }
}
