package com.moscicki.user_service.controller;

import com.moscicki.user_service.entities.notification.NotificationType;
import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.repository.SpotNotificationDefinitionRepository;
import com.moscicki.user_service.repository.UserRepository;
import com.moscicki.user_service.repository.spot.SpotRepository;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SpotNotificationDefinitionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    SpotNotificationDefinitionRepository spotNotificationDefinitionRepository;

    private User user;
    private Spot spot;


    @BeforeEach
    void setup() {
        spotRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        spot = spotRepository.save(TestUtils.createRiverSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));
    }

    @Test
    void createSpotNotificationDefinition_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS);

        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(post("/users/" + nonExistingUserId + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }


    @Test
    void createSpotNotificationDefinition_shouldReturnError_spotNotFound() throws Exception {
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS);

        String message = "No spot with given id: " + nonExistingSpotId;

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createSpotNotificationDefinition_shouldReturnError_userDoNotMatchSpot() throws Exception {
        User anotherUser = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot anotherSpot = spotRepository.save(TestUtils.createRiverSpot(null, anotherUser, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));

        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, anotherSpot, 10, 7, NotificationType.SMS);

        String message = "User do not match spot";

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + anotherSpot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createSpotNotificationDefinition_shouldReturnError_wrongHour() throws Exception {
        int hour = -15;
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, hour, 7, NotificationType.SMS);

        String message = "Wrong hour: " + hour;

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createSpotNotificationDefinition_shouldReturnError_wrongDayOfWeek() throws Exception {
        int dayOfWeek = 8;
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, dayOfWeek, NotificationType.SMS);

        String message = "Wrong day of week: " + dayOfWeek;

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createSpotNotificationDefinition_shouldReturnError_notificationTypesEmpty() throws Exception {
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, 7, null);

        String message = "Notification type can not be null";

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createSpotNotificationDefinition_shouldReturnError_notificationTypesContainWrongType() throws Exception {
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS);
        String type = "WRONG TYPE";

        String message = "No such type: " + type;

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition, type)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createSpotNotificationDefinition_shouldSucceed() throws Exception {
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS);

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.spotId").value(spot.getId()))
                .andExpect(jsonPath("$.hour").value(10))
                .andExpect(jsonPath("$.dayOfWeek").value(7))
                .andExpect(jsonPath("$.type").value("SMS"));
    }

    @Test
    void getSpotNotificationDefinition_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS);

        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(get("/users/" + nonExistingUserId + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }


    @Test
    void getSpotNotificationDefinition_shouldReturnError_spotNotFound() throws Exception {
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS);

        String message = "No spot with given id: " + nonExistingSpotId;

        mockMvc.perform(get("/users/" + user.getId() + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void getSpotNotificationDefinition_shouldReturnError_userDoNotMatchSpot() throws Exception {
        User anotherUser = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot anotherSpot = spotRepository.save(TestUtils.createRiverSpot(null, anotherUser, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));

        SpotNotificationDefinition notificationDefinition = TestUtils.createNotificationDefinition(null, anotherSpot, 10, 7, NotificationType.SMS);

        String message = "User do not match spot";

        mockMvc.perform(get("/users/" + user.getId() + "/spots/" + anotherSpot.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }


    @Test
    void getSpotNotificationDefinition_shouldSucceed() throws Exception {
        spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));

        mockMvc.perform(get("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateSpotNotificationDefinition_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        notificationDefinition.setType(NotificationType.EMAIL);
        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(put("/users/" + nonExistingUserId + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }


    @Test
    void updateSpotNotificationDefinition_shouldReturnError_spotNotFound() throws Exception {
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        notificationDefinition.setType(NotificationType.EMAIL);

        String message = "No spot with given id: " + nonExistingSpotId;

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateSpotNotificationDefinition_shouldReturnError_userDoNotMatchSpot() throws Exception {
        User anotherUser = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot anotherSpot = spotRepository.save(TestUtils.createRiverSpot(null, anotherUser, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));

        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        notificationDefinition.setType(NotificationType.EMAIL);

        String message = "User do not match spot";

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + anotherSpot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateSpotNotificationDefinition_shouldReturnError_wrongHour() throws Exception {

        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        int hour = -15;
        notificationDefinition.setExecutionHour(hour);

        String message = "Wrong hour: " + hour;

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateSpotNotificationDefinition_shouldSucceed() throws Exception {
        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        int hour = 2;
        notificationDefinition.setExecutionHour(hour);

        mockMvc.perform(post("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationDefinitionRequest(notificationDefinition)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.spotId").value(spot.getId()))
                .andExpect(jsonPath("$.hour").value(hour))
                .andExpect(jsonPath("$.dayOfWeek").value(7))
                .andExpect(jsonPath("$.type").value("SMS"));
    }

    @Test
    void deleteSpotNotificationDefinition_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        SpotNotificationDefinition notificationDefinition2= spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.EMAIL));
        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(delete("/users/" + nonExistingUserId + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationListRequest(Arrays.asList(notificationDefinition, notificationDefinition2))))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }


    @Test
    void deleteSpotNotificationDefinition_shouldReturnError_spotNotFound() throws Exception {
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        SpotNotificationDefinition notificationDefinition2= spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.EMAIL));
        notificationDefinition.setType(NotificationType.EMAIL);

        String message = "No spot with given id: " + nonExistingSpotId;

        mockMvc.perform(delete("/users/" + user.getId() + "/spots/" + nonExistingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationListRequest(Arrays.asList(notificationDefinition, notificationDefinition2))))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void deleteSpotNotificationDefinition_shouldReturnError_userDoNotMatchSpot() throws Exception {
        User anotherUser = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot anotherSpot = spotRepository.save(TestUtils.createRiverSpot(null, anotherUser, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));

        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        SpotNotificationDefinition notificationDefinition2= spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.EMAIL));
        notificationDefinition.setType(NotificationType.EMAIL);

        String message = "User do not match spot";

        mockMvc.perform(delete("/users/" + user.getId() + "/spots/" + anotherSpot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationListRequest(Arrays.asList(notificationDefinition, notificationDefinition2))))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void deleteSpotNotificationDefinition_shouldSucceed() throws Exception {
        SpotNotificationDefinition notificationDefinition = spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.SMS));
        SpotNotificationDefinition notificationDefinition2= spotNotificationDefinitionRepository.save(TestUtils.createNotificationDefinition(null, spot, 10, 7, NotificationType.EMAIL));
        notificationDefinition.setType(NotificationType.EMAIL);


        mockMvc.perform(delete("/users/" + user.getId() + "/spots/" + spot.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotNotificationListRequest(Arrays.asList(notificationDefinition, notificationDefinition2))))
                .andExpect(status().isOk());

        Assertions.assertTrue(spotNotificationDefinitionRepository.findById(notificationDefinition.getId()).isEmpty());
        Assertions.assertTrue(spotNotificationDefinitionRepository.findById(notificationDefinition2.getId()).isEmpty());
    }

}
