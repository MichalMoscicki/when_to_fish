package com.moscicki.user_service.utils;

import com.google.gson.Gson;
import com.moscicki.user_service.dto.spot.SpotType;
import com.moscicki.user_service.entities.notification.NotificationType;
import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.LakeSpot;
import com.moscicki.user_service.entities.spot.RiverSpot;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.translator.UserTranslator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {

    private static final Gson gson = new Gson();


    public static String createUserRequest(Long id, String email, String name, String lastName) {
        Gson gson = new Gson();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", id);
        jsonMap.put("email", email);
        jsonMap.put("name", name);
        jsonMap.put("lastName", lastName);
        return  gson.toJson(jsonMap);
    }

    public static User createUser(String email, String name, String lastName){
        return new User(null, email, name, lastName);
    };

    public static Spot createLakeSpot(String id, User user, BigDecimal lat,BigDecimal lon, String spotName, String lakeName) {
        LakeSpot spot = new LakeSpot();
        spot.setId(id);
        spot.setUser(user);
        spot.setLat(lat);
        spot.setLon(lon);
        spot.setSpotName(spotName);
        spot.setLakeName(lakeName);
        return spot;
    }

    public static Spot createRiverSpot(String id, User user, BigDecimal lat,BigDecimal lon, String spotName, String riverName, Integer minOptimalWaterLevel, Integer maxOptimalWaterLevel, Long measurementStationId) {
        RiverSpot spot = new RiverSpot();
        spot.setId(id);
        spot.setUser(user);
        spot.setLat(lat);
        spot.setLon(lon);
        spot.setSpotName(spotName);
        spot.setRiverName(riverName);
        spot.setMaxOptimalWaterLevel(maxOptimalWaterLevel);
        spot.setMinOptimalWaterLevel(minOptimalWaterLevel);
        spot.setMeasurementStationId(measurementStationId);
        return spot;
    }

    public static SpotNotificationDefinition createNotificationDefinition(String id, Spot spot, int hour, int dayOfWeek, NotificationType type) {
        SpotNotificationDefinition notificationDefinition = new SpotNotificationDefinition();
        notificationDefinition.setId(id);
        notificationDefinition.setSpotId(spot.getId());
        notificationDefinition.setExecutionHour(hour);
        notificationDefinition.setDayOfWeek(dayOfWeek);
        notificationDefinition.setType(type);
        return notificationDefinition;
    }

    public static String createLakeRequest(LakeSpot spot) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("type", SpotType.LAKE_SPOT);
        jsonMap.put("id", spot.getId());
        jsonMap.put("lon", spot.getLon());
        jsonMap.put("lat", spot.getLat());
        jsonMap.put("spotName", spot.getSpotName());
        jsonMap.put("lakeName", spot.getLakeName());
        if (spot.getUser() != null) {
            jsonMap.put("user", UserTranslator.translate(spot.getUser()));
        }
        return  gson.toJson(jsonMap);
    }

    public static String createRiverRequest(RiverSpot spot) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("spotName", spot.getSpotName());
        jsonMap.put("id", spot.getId());
        jsonMap.put("type", SpotType.RIVER_SPOT);
        jsonMap.put("lon", spot.getLon());
        jsonMap.put("lat", spot.getLat());
        jsonMap.put("riverName", spot.getRiverName());
        jsonMap.put("measurementStationId", spot.getMeasurementStationId());
        jsonMap.put("minOptimalWaterLevel", spot.getMinOptimalWaterLevel());
        jsonMap.put("maxOptimalWaterLevel", spot.getMaxOptimalWaterLevel());
        if (spot.getUser() != null) {
            jsonMap.put("user", UserTranslator.translate(spot.getUser()));
        }
        return  gson.toJson(jsonMap);
    }

    public static String createSpotListRequest(List<Spot> spots) {
        List<String> spotJsons = new ArrayList<>();
        for (Spot spot : spots) {
            if (spot instanceof RiverSpot) {
                spotJsons.add(createRiverRequest((RiverSpot) spot));
            } else if (spot instanceof LakeSpot) {
                spotJsons.add(createLakeRequest((LakeSpot) spot));
            }
        }
        return "[" + String.join(",", spotJsons) + "]";
    }

    public static String createSpotNotificationDefinitionRequest(SpotNotificationDefinition notificationDefinition) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", notificationDefinition.getId());
        jsonMap.put("spotId", notificationDefinition.getSpotId());
        jsonMap.put("hour", notificationDefinition.getExecutionHour());
        jsonMap.put("dayOfWeek", notificationDefinition.getDayOfWeek());
        jsonMap.put("type", notificationDefinition.getType());
        return  gson.toJson(jsonMap);
    }

    public static String createSpotNotificationDefinitionRequest(SpotNotificationDefinition notificationDefinition, String type) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", notificationDefinition.getId());
        jsonMap.put("spotId", notificationDefinition.getSpotId());
        jsonMap.put("hour", notificationDefinition.getExecutionHour());
        jsonMap.put("dayOfWeek", notificationDefinition.getDayOfWeek());
        jsonMap.put("type", type);
        return  gson.toJson(jsonMap);
    }

    public static String createSpotNotificationListRequest(List<SpotNotificationDefinition> notificationDefinitions) {
        List<String> notificationDefinitionsJsons = new ArrayList<>();
        for (SpotNotificationDefinition spotNotificationDefinition : notificationDefinitions) {
            notificationDefinitionsJsons.add(createSpotNotificationDefinitionRequest(( spotNotificationDefinition)));
        }
        return "[" + String.join(",", notificationDefinitionsJsons) + "]";
    }

}
