package com.mushroomapp.app.service;

import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public void deleteById(UUID id) {
        this.userRepository.deleteById(id);
    }

    public User getUserById(UUID id) {
        return this.userRepository.getReferenceById(id);
    }

    public User getUserByToken(String token) {
        return this.userRepository.getReferenceByToken(token);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void followUser(User a, User b) {
        a.follow(b);
        b.addFollower(a);
    }

    public void unfollowUser(User a, User b) {
        a.unfollow(b);
        b.removeFollower(a);
    }
}
