package com.mushroomapp.app.controller;

import com.google.api.gax.rpc.NotFoundException;
import com.mushroomapp.app.controller.format.request.UserCreationRequest;
import com.mushroomapp.app.controller.format.response.FollowUserResponse;
import com.mushroomapp.app.controller.format.response.UnfollowUserResponse;
import com.mushroomapp.app.controller.format.response.UserCreationResponse;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
    public ResponseEntity<UserCreationResponse> createUser(@RequestBody UserCreationRequest userCreationRequest, HttpServletRequest httpRequest) throws SQLException {

        String token = this.httpRequestReader.getId(httpRequest);
        String username = userCreationRequest.username;

        User user = new User();
        user.setUsername(username);
        user.setToken(token);

        try {
            User createdUser = this.userService.save(user);

            System.out.println("Created user " + createdUser);

            UserCreationResponse response = UserCreationResponse
                    .builder()
                    .userId(createdUser.getId())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new SQLException("could not insert user: " + userCreationRequest.username);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) throws SQLException {
        try {
            this.userService.deleteById(id);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            throw new SQLException("could not delete user with id " + id);
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
    public ResponseEntity<FollowUserResponse> userFollowsUser(@PathVariable UUID id, HttpServletRequest request) throws BadRequestException {

        String token = httpRequestReader.getId(request);

        Optional<User> currentUser = this.userService.getUserByToken(token);
        if (currentUser.isEmpty()) throw new NoSuchElementException("User does not exist with token " + token);

        Optional<User> userToFollow = this.userService.getUserById(id);
        if (userToFollow.isEmpty()) throw new BadRequestException("User does not exist with id " + id);

        userService.followUser(
                currentUser.get(),
                userToFollow.get()
        );

        FollowUserResponse response = FollowUserResponse
                .builder()
                .followedId(userToFollow.get().getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/follow/{id}")
    public ResponseEntity<UnfollowUserResponse> userUnfollowsUser(@PathVariable UUID id, HttpServletRequest request) throws BadRequestException {

        String token = httpRequestReader.getId(request);

        Optional<User> currentUser = this.userService.getUserByToken(token);
        if (currentUser.isEmpty()) throw new NoSuchElementException("User does not exist with token " + token);

        Optional<User> userToFollow = this.userService.getUserById(id);
        if (userToFollow.isEmpty()) throw new BadRequestException("User does not exist with id " + id);

        userService.unfollowUser(
                currentUser.get(),
                userToFollow.get()
        );

        UnfollowUserResponse response = UnfollowUserResponse
                .builder()
                .followedId(userToFollow.get().getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<List<User>> getUsersFollowedByUser(@PathVariable UUID id) {
        Optional<User> user = this.userService.getUserById(id);

        if(user.isEmpty()) throw new NoSuchElementException("could not find user with id " + id);

        System.out.println("User " + user.get() + " is following");
        for(User u : user.get().getFollowers()) System.out.println(u);

        return ResponseEntity.ok(user.get().getFollowing());
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<List<User>> getFollowersOfUser(@PathVariable UUID id) {
        Optional<User> user = this.userService.getUserById(id);

        if(user.isEmpty()) throw new NoSuchElementException("could not find user with id " + id);

        return ResponseEntity.ok(user.get().getFollowers());
    }
}
