package com.mushroomapp.app.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Media;
import com.mushroomapp.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaService mediaService;

    public Optional<User> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getName();

        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(token);
            String uid = userRecord.getUid();

            return this.userRepository.findByToken(uid);
        } catch(Exception e) {
            return Optional.empty();
        }
    }
    public User save(User user) {
        if(user.getProfilePicture() == null) {
            Optional<Media> profilePicture = this.mediaService.getMediaByFilename("default-profile-picture.png");
            profilePicture.ifPresent(user::setProfilePicture);
        }
        return this.userRepository.save(user);
    }

    public void deleteById(UUID id) {
        this.userRepository.deleteById(id);
    }

    public Optional<User> getUserById(UUID id) {
        return this.userRepository.findById(id);
    }

    public Optional<User> getUserByToken(String token) {
        return this.userRepository.findByToken(token);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public boolean currentUserFollows(User user) {
        Optional<User> currentUser =  this.currentUser();
        if(currentUser.isPresent())
            return currentUser
                    .get()
                    .getFollowing()
                    .contains(user);
        throw new NoSuchElementException("Could not identify the current user");
    }

    @Transactional
    public void followUser(User a, User b) {
        a.follow(b);
        b.addFollower(a);
    }

    @Transactional
    public void unfollowUser(User a, User b) {
        a.unfollow(b);
        b.removeFollower(a);
    }
}
