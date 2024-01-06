package com.mushroomapp.app.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfiguration {

    @Bean
    public FirebaseApp createFireBaseApp() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("C:\\Users\\ratbo\\Documents\\Code\\SpringBoot\\mushroom-v1\\app\\src\\main\\java\\com\\mushroomapp\\app\\security\\serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        System.out.println("Firebase auth config initialization");

        if(FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);
        return FirebaseApp.getInstance();
    }

    @Bean
    @DependsOn(value = "createFireBaseApp")
    public FirebaseAuth createFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

}
