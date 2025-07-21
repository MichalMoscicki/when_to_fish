package com.moscicki.user_service.dto.notification;

import com.moscicki.user_service.dto.spot.SpotDTO;

public record OrderNotificationDTO(
        String type,
        SpotDTO spotDTO
) {
}
