package com.moscicki.user_service.validation;

import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.exception.UserServiceException;

import java.util.List;
import java.util.Objects;

public class SpotNotificationDefinitionValidator {

    public static void validate(SpotNotificationDefinition spotNotificationDefinition) {
        IdValidator.validate(spotNotificationDefinition.getId());
        if (spotNotificationDefinition.getSpotId() == null || spotNotificationDefinition.getSpotId().isBlank()) {
            throw new UserServiceException("SpotId name must not be empty or null");
        }
        IdValidator.validate(spotNotificationDefinition.getSpotId());
        if (spotNotificationDefinition.getExecutionHour() < 1 || spotNotificationDefinition.getExecutionHour() > 24) {
            throw new UserServiceException("Wrong hour: " + spotNotificationDefinition.getExecutionHour());
        }
        if (spotNotificationDefinition.getDayOfWeek() < 1 || spotNotificationDefinition.getDayOfWeek() > 7) {
            throw new UserServiceException("Wrong day of week: " + spotNotificationDefinition.getDayOfWeek());
        }
        if (spotNotificationDefinition.getType() == null) {
            throw new UserServiceException("Notification type can not be null");
        }


    }

    public static void validateEdit(Spot spot, SpotNotificationDefinition spotNotificationDefinition, SpotNotificationDefinition spotNotificationDefinitionParam) {
        if (!Objects.equals(spot.getId(), spotNotificationDefinitionParam.getSpotId())) {
            throw new UserServiceException("Notification do not match the spot");
        }
        if (!Objects.equals(spotNotificationDefinition.getSpotId(), spotNotificationDefinitionParam.getSpotId())) {
            throw new UserServiceException("Action forbidden");
        }
        validate(spotNotificationDefinitionParam);
    }

    public static void validateDelete(Spot spot, List<SpotNotificationDefinition> spotNotificationDefinitions) {
        for (SpotNotificationDefinition spotNotificationDefinition : spotNotificationDefinitions) {
            if (!Objects.equals(spot.getId(), spotNotificationDefinition.getSpotId())) {
                throw new UserServiceException("Action forbidden");
            }
        }
    }
}
