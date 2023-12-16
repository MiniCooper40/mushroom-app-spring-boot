package com.mushroomapp.app;

import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.service.DirectoryService;
import com.mushroomapp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class OnStartup implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private DirectoryService directoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        User u1 = new User();
//        u1.setToken("123901m2049m");
//        u1.setUsername("Robbie123");
//        userService.save(u1);

        directoryService.save("C:\\Users\\ratbo\\Documents\\Code\\SpringBoot\\mushroom-v1\\app\\src\\main\\resources\\static\\");
    }
}
