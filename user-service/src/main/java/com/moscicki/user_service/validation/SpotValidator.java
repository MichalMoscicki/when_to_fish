package com.moscicki.user_service.validation;

import com.moscicki.user_service.entities.spot.LakeSpot;
import com.moscicki.user_service.entities.spot.RiverSpot;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.exception.UserServiceException;

import java.util.List;
import java.util.Objects;

public class SpotValidator {

    public static void validate(Spot spot) {
        IdValidator.validate(spot.getId());
        if (spot.getSpotName() == null || spot.getSpotName().isBlank()) {
            throw new UserServiceException("Spot name must not be empty or null");
        }
        if (spot.getLon() == null || spot.getLat() == null) {
            throw new UserServiceException("Wrong coordinates");
        }

        //todo additional coordinates validation

        validateSpotDetails(spot);

    }

    private static void validateSpotDetails(Spot spot) {
        if (spot instanceof RiverSpot) {
            validate((RiverSpot) spot);
        }
        if (spot instanceof LakeSpot) {
            validate((LakeSpot) spot);
        }

    }

    private static void validate(LakeSpot spot) {
        if (spot.getLakeName() == null || spot.getLakeName().isBlank()) {
            throw new UserServiceException("Lake name must not be empty or null");
        }
    }

    private static void validate(RiverSpot spot) {
        if (spot.getRiverName() == null || spot.getRiverName().isBlank()) {
            throw new UserServiceException("River name must not be empty or null");
        }
        if (spot.getMeasurementStationId() == null) {
            throw new UserServiceException("No measurement station id");
        }
        if (spot.getMinOptimalWaterLevel() == null || spot.getMinOptimalWaterLevel() <= 0) {
            throw new UserServiceException("Minimum optimal water level incorrect");
        }
        if (spot.getMaxOptimalWaterLevel() == null || spot.getMaxOptimalWaterLevel() <= 0) {
            throw new UserServiceException("Maximum optimal water level incorrect");
        }
        if (spot.getMinOptimalWaterLevel() > spot.getMaxOptimalWaterLevel()) {
            throw new UserServiceException("Maximum optimal water level is smaller then minimum optimal water level");
        }
    }

    public static void validateDelete(User user, List<Spot> spots) {
        for (Spot spot : spots) {
            if (!Objects.equals(user.getId(), spot.getUser().getId())) {
                throw new UserServiceException("Action forbidden");
            }
        }
    }
}
