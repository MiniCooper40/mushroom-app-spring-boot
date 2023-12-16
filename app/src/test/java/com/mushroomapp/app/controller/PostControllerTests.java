package com.mushroomapp.app.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PostControllerTests {

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

}
