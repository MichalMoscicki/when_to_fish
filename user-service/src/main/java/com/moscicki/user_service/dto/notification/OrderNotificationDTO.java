package com.moscicki.user_service.dto.notification;


public record OrderNotificationDTO(
        String type,
        String userId,
        String spotId,
        String lan,
        String lat,
        String spotType
) {
}
