package com.moscicki.user_service.controller;


import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.repository.UserRepository;
import com.moscicki.user_service.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_shouldReturnError_emailIncorrect() throws Exception {
        String email = "";
        String name = "";
        String lastName = "";
        String message = "Email can not be null or empty";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, name, lastName)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createUser_shouldReturnError_emailAlreadyTaken() throws Exception {
        String email = "existing@example.com";
        String name = "Jane";
        String lastName = "Doe";
        String message = "User with given email already exists";

        userRepository.save(TestUtils.createUser(email, name, lastName));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, "Another", "User")))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createUser_shouldReturnError_nameIncorrect() throws Exception {
        String email = "test@example.com";
        String name = "";
        String lastName = "Doe";
        String message = "Name can not be null or empty";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, name, lastName)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createUser_shouldReturnError_lastNameIncorrect() throws Exception {
        String email = "test@example.com";
        String name = "John";
        String lastName = "";
        String message = "LastName can not be null or empty";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, name, lastName)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createUser_shouldSucceed() throws Exception {
        String email = "newuser@example.com";
        String name = "Alice";
        String lastName = "Smith";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, name, lastName)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @Test
    void getUser_shouldReturnUser_whenUserExists() throws Exception {
        User user = userRepository.save(TestUtils.createUser("exmaple@gmail.com", "Anthony", "Hopkins"));

        mockMvc.perform(get("/users/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()));
    }

    @Test
    void getUser_shouldReturnError_whenUserDoesNotExist() throws Exception {
        String nonExistentUserId = String.valueOf(UUID.randomUUID());;
        String message = "No user with given id: " + nonExistentUserId;

        mockMvc.perform(get("/users/{id}", nonExistentUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    //todo zrobić walidację na id
    @Test
    void getUser_shouldReturnError_whenIdIsInvalid() throws Exception {
        String invalidId = "atj";
        String message = "Invalid id: " + invalidId;
        mockMvc.perform(get("/users/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateUser_shouldReturnError_userNotFound() throws Exception {
        Long userId = 999L; // Nonexistent ID
        String email = "new@example.com";
        String name = "Ghost";
        String lastName = "User";
        String message = "No user with given id: " + userId;

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, name, lastName)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateUser_shouldReturnError_emailAlreadyTaken() throws Exception {
        String email = "new@example.com";
        String name = "Ghost";
        String lastName = "User";
        User user = userRepository.save(TestUtils.createUser(email, name, lastName));
        String message = "Email can not be edited";

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, "another@email.com", name, lastName)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateUser_shouldReturnError_nameIncorrect() throws Exception {
        String email = "new@example.com";
        String name = "Ghost";
        String lastName = "User";
        User user = userRepository.save(TestUtils.createUser(email, name, lastName));
        String message = "Name can not be null or empty";

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, "", lastName)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateUser_shouldReturnError_lastNameIncorrect() throws Exception {
        String email = "new@example.com";
        String name = "Ghost";
        String lastName = "User";
        User user = userRepository.save(TestUtils.createUser(email, name, lastName));
        String message = "LastName can not be null or empty";

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, name, "")))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateUser_shouldSucceed() throws Exception {
        String email = "new@example.com";
        String name = "Ghost";
        String lastName = "User";
        User user = userRepository.save(TestUtils.createUser(email, name, lastName));

        name = "newName";
        lastName = "newLastName";

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createUserRequest(null, email, name, lastName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @Test
    void deleteUser_shouldSucceed_whenUserExists() throws Exception {
        String email = "deleteuser@example.com";
        String name = "Delete";
        String lastName = "Me";
        User user = userRepository.save(TestUtils.createUser(email, name, lastName));

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertTrue(userRepository.findById(user.getId()).isEmpty());

    }

    @Test
    void getUser_shouldReturnError_whenUserDeleted() throws Exception {
        User user = userRepository.save(TestUtils.createUser("exmaple@gmail.com", "Anthony", "Hopkins"));

        mockMvc.perform(get("/users/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()));

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertTrue(userRepository.findById(user.getId()).isEmpty());
    }

}
