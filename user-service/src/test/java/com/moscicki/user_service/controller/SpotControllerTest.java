package com.moscicki.user_service.controller;


import com.moscicki.user_service.dto.spot.SpotType;
import com.moscicki.user_service.entities.spot.LakeSpot;
import com.moscicki.user_service.entities.spot.RiverSpot;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SpotControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SpotRepository spotRepository;


    @BeforeEach
    void setup() {
        spotRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createLakeSpot_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());;
        Spot lakeSpot = TestUtils.createLakeSpot(null, null, null, null, null, null);
        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(post("/users/" + nonExistingUserId + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) lakeSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createLakeSpot_shouldReturnError_wrongName() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot lakeSpot = TestUtils.createLakeSpot(null, null, null, null, null, null);
        String message = "Spot name must not be empty or null";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) lakeSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createLakeSpot_shouldReturnError_wrongCoordinates() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot lakeSpot = TestUtils.createLakeSpot(null, null, null, new BigDecimal("52.29558808401241"), "Spot name", "Lake");
        String message = "Wrong coordinates";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) lakeSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createLakeSpot_shouldReturnError_wrongLakeName() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot lakeSpot = TestUtils.createLakeSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "");

        String message = "Lake name must not be empty or null";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) lakeSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createRiverSpot_shouldReturnError_wrongRiverName() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot riverSpot = TestUtils.createRiverSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", null, 1, 1, 2L);
        String message = "River name must not be empty or null";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createRiverRequest((RiverSpot) riverSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createRiverSpot_shouldReturnError_noStationId() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot riverSpot = TestUtils.createRiverSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "RiverName", 1, 1, null);
        String message = "No measurement station id";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createRiverRequest((RiverSpot) riverSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createRiverSpot_shouldReturnError_minOptimalWaterIncorrect() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot riverSpot = TestUtils.createRiverSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "RiverName", null, 1, 1L);

        String message = "Minimum optimal water level incorrect";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createRiverRequest((RiverSpot) riverSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createRiverSpot_shouldReturnError_maxOptimalWaterIncorrect() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot riverSpot = TestUtils.createRiverSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "RiverName", 1, null, 1L);

        String message = "Maximum optimal water level incorrect";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createRiverRequest((RiverSpot) riverSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createRiverSpot_shouldReturnError_waterLevelsIncorrect() throws Exception {
        User user = userRepository.save(TestUtils.createUser("example@mail.com", "John", "Doe"));
        Spot riverSpot = TestUtils.createRiverSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "RiverName", 10, 1, 1L);

        String message = "Maximum optimal water level is smaller then minimum optimal water level";

        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createRiverRequest((RiverSpot) riverSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createLakeSpot_shouldSucceed() throws Exception {
        User user = userRepository.save(TestUtils.createUser("lakeuser@mail.com", "Alice", "Lake"));
        LakeSpot lakeSpot = (LakeSpot) TestUtils.createLakeSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "Lake");


        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest(lakeSpot)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.user").isNotEmpty())
                .andExpect(jsonPath("$.type").value(SpotType.LAKE_SPOT))
                .andExpect(jsonPath("$.spotName").value(lakeSpot.getSpotName()))
                .andExpect(jsonPath("$.lakeName").value(lakeSpot.getLakeName()))
                .andExpect(jsonPath("$.lon").value(lakeSpot.getLon()))
                .andExpect(jsonPath("$.lat").value(lakeSpot.getLat()));

    }

    @Test
    void createRiverSpot_shouldSucceed() throws Exception {
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));
        RiverSpot riverSpot = (RiverSpot) TestUtils.createRiverSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "RiverName", 1, 10, 1L);


        mockMvc.perform(post("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createRiverRequest(riverSpot)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.user").isNotEmpty())
                .andExpect(jsonPath("$.type").value(SpotType.RIVER_SPOT))
                .andExpect(jsonPath("$.minOptimalWaterLevel").value((riverSpot.getMinOptimalWaterLevel())))
                .andExpect(jsonPath("$.maxOptimalWaterLevel").value(riverSpot.getMaxOptimalWaterLevel()))
                .andExpect(jsonPath("$.measurementStationId").value(riverSpot.getMeasurementStationId()))
                .andExpect(jsonPath("$.spotName").value(riverSpot.getSpotName()))
                .andExpect(jsonPath("$.riverName").value(riverSpot.getRiverName()))
                .andExpect(jsonPath("$.lon").value(riverSpot.getLon()))
                .andExpect(jsonPath("$.lat").value(riverSpot.getLat()));
    }

    @Test
    void getSpots_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());
        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(get("/users/" + nonExistingUserId + "/spots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void getSpots_shouldReturnEmptySpotList() throws Exception {
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));

        mockMvc.perform(get("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getSpots_shouldReturnSpotList() throws Exception {
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));
        spotRepository.save(TestUtils.createRiverSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "RiverName", 1, 10, 1L));
        spotRepository.save(TestUtils.createLakeSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "Lake name"));


        mockMvc.perform(get("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateSpot_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());;
        Spot lakeSpot = TestUtils.createLakeSpot(null, null, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "Lake name");
        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(put("/users/" + nonExistingUserId + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) lakeSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateSpot_shouldReturnError_spotIdIsNull() throws Exception {
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));
        Spot lakeSpot = TestUtils.createLakeSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "Lake name");

        String message = "Spot id can not be null";

        mockMvc.perform(put("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) lakeSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateSpot_shouldReturnError_spotNotFound() throws Exception {
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));
        String nonExistingSpotId = String.valueOf(UUID.randomUUID());
        Spot lakeSpot = TestUtils.createLakeSpot(nonExistingSpotId, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "Spot name", "Lake name");
        String message = "No spot with given id: " + nonExistingSpotId + " This error can occur if spot type was updated";

        mockMvc.perform(put("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) lakeSpot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateSpot_shouldReturnError_userDoNotMatchTheSpot() throws Exception {
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));
        User anotherUser = userRepository.save(TestUtils.createUser("riveruser1@mail.com", "John", "Doe"));

        Spot spot = spotRepository.save(TestUtils.createLakeSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName"));
        spot.setUser(anotherUser);
        String message = "User do not match spot";

        mockMvc.perform(put("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest((LakeSpot) spot)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void updateLakeSpot_shouldSucceed() throws Exception {
        User user = userRepository.save(TestUtils.createUser("lakeuser@mail.com", "Alice", "Lake"));
        LakeSpot spot = (LakeSpot) spotRepository.save(TestUtils.createLakeSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName"));

        String changedLakeName = "NewLakeName";
        spot.setLakeName(changedLakeName);

        mockMvc.perform(put("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createLakeRequest(spot)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.user").isNotEmpty())
                .andExpect(jsonPath("$.type").value(SpotType.LAKE_SPOT))
                .andExpect(jsonPath("$.spotName").value(spot.getSpotName()))
                .andExpect(jsonPath("$.lakeName").value(changedLakeName))
                .andExpect(jsonPath("$.lon").value(spot.getLon()))
                .andExpect(jsonPath("$.lat").value(spot.getLat()));

    }

    @Test
    void updateRiverSpot_shouldSucceed() throws Exception {
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));
        RiverSpot spot = (RiverSpot) spotRepository.save(TestUtils.createRiverSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));

        String changedSpotName = "NewSpotName";
        String changedRiverName = "NewRiverName";
        spot.setSpotName(changedSpotName);
        spot.setRiverName(changedRiverName);

        mockMvc.perform(put("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createRiverRequest(spot))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.user").isNotEmpty())
                .andExpect(jsonPath("$.type").value(SpotType.RIVER_SPOT))
                .andExpect(jsonPath("$.minOptimalWaterLevel").value(spot.getMinOptimalWaterLevel()))
                .andExpect(jsonPath("$.maxOptimalWaterLevel").value(spot.getMaxOptimalWaterLevel()))
                .andExpect(jsonPath("$.measurementStationId").value(spot.getMeasurementStationId()))
                .andExpect(jsonPath("$.riverName").value(changedRiverName))
                .andExpect(jsonPath("$.spotName").value(changedSpotName))
                .andExpect(jsonPath("$.lon").value(spot.getLon()))
                .andExpect(jsonPath("$.lat").value(spot.getLat()));
    }


    @Test
    void deleteSpots_shouldReturnError_userNotFound() throws Exception {
        String nonExistingUserId = String.valueOf(UUID.randomUUID());
        User user = userRepository.save(TestUtils.createUser("riveruser@mail.com", "Bob", "River"));
        Spot spot = spotRepository.save(TestUtils.createLakeSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName"));

        String message = "No user with given id: " + nonExistingUserId;

        mockMvc.perform(delete("/users/" + nonExistingUserId + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotListRequest(new ArrayList<>())))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void deleteSpots_shouldReturnError_userIOnObjectDoNotMatchUserFromPath() throws Exception {
        User user = userRepository.save(TestUtils.createUser("lakeuser@mail.com", "Alice", "Lake"));
        User anotherUser = userRepository.save(TestUtils.createUser("lakeuser@mail.com", "Alice", "Lake"));
        LakeSpot lakeSpot = (LakeSpot) spotRepository.save(TestUtils.createLakeSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName"));
        RiverSpot riverSpot = (RiverSpot) spotRepository.save(TestUtils.createRiverSpot(null, anotherUser, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));

        String message = "Action forbidden";

        mockMvc.perform(delete("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotListRequest(Arrays.asList(lakeSpot, riverSpot))))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(message));

    }

    @Test
    void deleteSpots_shouldSucceed() throws Exception {
        User user = userRepository.save(TestUtils.createUser("lakeuser@mail.com", "Alice", "Lake"));
        LakeSpot lakeSpot = (LakeSpot) spotRepository.save(TestUtils.createLakeSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName"));
        RiverSpot riverSpot = (RiverSpot) spotRepository.save(TestUtils.createRiverSpot(null, user, new BigDecimal("20.97862601358885"), new BigDecimal("52.29558808401241"), "FishingHole", "LakeName", 10, 20, 100L));


        mockMvc.perform(delete("/users/" + user.getId() + "/spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.createSpotListRequest(Arrays.asList(lakeSpot, riverSpot))))
                .andExpect(status().isOk());

        Assertions.assertTrue(spotRepository.findBySpotParam(lakeSpot).isEmpty());
        Assertions.assertTrue(spotRepository.findBySpotParam(riverSpot).isEmpty());
    }

    //todo notifications deleted

}
