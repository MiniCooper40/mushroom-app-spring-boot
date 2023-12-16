package com.mushroomapp.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class MockedUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final String BASE_URL = "/v1/users";

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    private final List<User> users = List.of(
            new User(UUID.fromString("0d95d28f-4e81-4eb3-a0e9-5fa36e351ea9"), "123123123", "user1", null, null, null),
            new User(UUID.fromString("19d6da98-1869-451f-9d63-7ff837cd7258"), "abcabcabc", "user2", null, null, null)
    );


    @Test
    void saveUserCalledOnce() throws Exception {

        User user = users.get(0);

        when(userService.save(any())).thenAnswer(i -> i.getArguments()[0]);


        this.mockMvc
                .perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(
                                writer.writeValueAsString(user)
                        ))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andExpect(content().string(containsString("username")))
                .andExpect(content().string(containsString(user.getToken())))
                .andExpect(content().string(containsString(user.getUsername())));

        verify(userService, times(1)).save(any());
    }

    @Test
    void deleteUserByIdCalledOnce() throws Exception {
        String URL = BASE_URL;

        String id = users.get(0).getId().toString();

        this.mockMvc
                .perform(delete(BASE_URL+"/"+id))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(any());
    }

    @Test
    void getUserByIdCalledOnce() throws Exception {

        User user = users.get(0);

        when(userService.getUserById(eq(user.getId()))).thenReturn(user);

        this.mockMvc
                .perform(get(BASE_URL+"/"+user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andExpect(content().string(containsString("username")))
                .andExpect(content().string(containsString("id")))
                .andExpect(content().string(containsString(user.getToken())))
                .andExpect(content().string(containsString(user.getUsername())))
                .andExpect(content().string(containsString(user.getId().toString())));

        verify(userService, times(1)).getUserById(user.getId());
    }

    @Test
    void getAllUsers() throws Exception {


        when(userService.getAllUsers()).thenReturn(users);

        this.mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andExpect(content().string(containsString("username")))
                .andExpect(content().string(containsString("id")))
                .andExpect(content().string(containsString(users.get(0).getToken())))
                .andExpect(content().string(containsString(users.get(0).getUsername())))
                .andExpect(content().string(containsString(users.get(0).getId().toString())))
                .andExpect(content().string(containsString(users.get(1).getToken())))
                .andExpect(content().string(containsString(users.get(1).getUsername())))
                .andExpect(content().string(containsString(users.get(1).getId().toString())));

        verify(userService, times(1)).getAllUsers();
    }
}
