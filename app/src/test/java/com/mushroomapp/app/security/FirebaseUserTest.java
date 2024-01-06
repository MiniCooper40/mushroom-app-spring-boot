package com.mushroomapp.app.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FirebaseUserTest {

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Test
    public void getFirebaseUser() throws FirebaseAuthException {
        UserRecord userRecord = this.firebaseAuth.getUser("QYFAZ0bIrAfMsxvD4oIXaTtXWS12");
        assertEquals("ratboy4066@yahoo.ca", userRecord.getEmail());
    }
}
