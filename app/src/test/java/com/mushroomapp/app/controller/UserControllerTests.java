package com.mushroomapp.app.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class UserControllerTests {

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseAuth firebaseAuth;

    private List<User> users;

    @Before
    public void reset() {
        users = new LinkedList<>();
        for(int i = 0; i < 10; i++) {
            User u = new User();
            u.setUsername("user"+i);
            u.setToken("token"+i);
            users.add(u);
        }

        for(User u : this.users) {
            userService.save(u);
            System.out.println(u);
        }
    }

    private List<String> usernames(List<User> users) {
        return users
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    private List<String> tokens(List<User> users) {
        return users
                .stream()
                .map(User::getToken)
                .collect(Collectors.toList());
    }

    private List<UUID> ids(List<User> users) {
        return users
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    private void assertUsersAreEqual(List<User> actual, List<User> expected) {
        assertThat(tokens(actual)).containsExactlyElementsOf(tokens(expected));
        assertThat(usernames(actual)).containsExactlyElementsOf(usernames(expected));
        assertThat(usernames(actual)).containsExactlyElementsOf(usernames(expected));
    }

    private void assertCurrentUsers(List<User> users) {
        assertUsersAreEqual(userService.getAllUsers(), users);
    }

    @Test
    public void containsAllUsers() {
        assertCurrentUsers(users);
    }

    @Test
    public void removeSingleUser() {
        User user = users.remove(0);
        userService.deleteById(user.getId());
        assertCurrentUsers(users);
    }

    @Test
    public void removeMultipleUsers() {
        User user = users.remove(0);
        userService.deleteById(user.getId());
        user = users.remove(1);
        userService.deleteById(user.getId());
        user = users.remove(2);
        userService.deleteById(user.getId());
        assertCurrentUsers(users);
    }

    @Test
    public void followUser() {
        String firebaseId = "QYFAZ0bIrAfMsxvD4oIXaTtXWS12";
        String username = "ratboy";

        User followingUser = new User();
        followingUser.setToken(firebaseId);
        followingUser.setUsername(username);

        List<User> following = followingUser.getFollowing();

        List<User> users = this.userService.getAllUsers().stream().filter(u -> u != followingUser).toList();

        for(User u : users) userService.followUser(followingUser, u);

        this.userService.save(followingUser);

        assertThat(following).containsExactlyElementsOf(users);
    }

    @Test
    public void unfollowUser() {
        String firebaseId = "QYFAZ0bIrAfMsxvD4oIXaTtXWS12";
        String username = "ratboy";

        User followingUser = new User();
        followingUser.setToken(firebaseId);
        followingUser.setUsername(username);

        List<User> following = followingUser.getFollowing();

        List<User> users = this.userService.getAllUsers().stream().filter(u -> u != followingUser).toList();

        for(User u : users) userService.followUser(followingUser, u);

        this.userService.save(followingUser);

        assertThat(following).containsExactlyElementsOf(users);

        for(User u : users) userService.unfollowUser(followingUser, u);

        assertThat(following).isEmpty();
    }
}
