package com.moscicki.prediction_service.dto;

public record OrderNotificationDTO(
        String type,
        String userId,
        String spotId,
        String lan,
        String lat,
        String spotType
) {
}
