package com.moscicki.user_service.translator;

import com.moscicki.user_service.dto.notification.SpotNotificationDefinitionDTO;
import com.moscicki.user_service.entities.notification.NotificationType;
import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.Spot;

import java.util.List;

public class SpotNotificationDefinitionTranslator {

    public static SpotNotificationDefinitionDTO translate(SpotNotificationDefinition spotNotificationDefinition) {
        return new SpotNotificationDefinitionDTO(
                spotNotificationDefinition.getId(),
                spotNotificationDefinition.getSpotId(),
                spotNotificationDefinition.getExecutionHour(),
                spotNotificationDefinition.getDayOfWeek(),
                translate(spotNotificationDefinition.getType()));
    }

    public static SpotNotificationDefinition translate(SpotNotificationDefinitionDTO spotNotificationDefinitionDTO) {
        return new SpotNotificationDefinition(
                spotNotificationDefinitionDTO.id(),
                spotNotificationDefinitionDTO.spotId(),
                spotNotificationDefinitionDTO.hour(),
                spotNotificationDefinitionDTO.dayOfWeek(),
                translate(spotNotificationDefinitionDTO.type()));
    }


    private static NotificationType translate(String type) {
        return NotificationType.getNotificationType(type);
    }

    private static String translate(NotificationType type) {
        return NotificationType.getString(type);
    }

    public static List<SpotNotificationDefinitionDTO> translateList(List<SpotNotificationDefinition> spots) {
        return spots.stream().map(SpotNotificationDefinitionTranslator::translate).toList();
    }

    public static List<SpotNotificationDefinition> translateDTOList(List<SpotNotificationDefinitionDTO> spots) {
        return spots.stream().map(SpotNotificationDefinitionTranslator::translate).toList();
    }
}
