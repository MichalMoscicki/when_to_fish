package com.moscicki.user_service.dto.notification;

public record SpotNotificationDefinitionDTO(
        String id,
        String spotId,
        int hour,
        int dayOfWeek,
        String type
) {
}
