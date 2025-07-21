package com.moscicki.user_service.dto.spot;


import com.moscicki.user_service.dto.user.UserDTO;

import java.math.BigDecimal;

public record RiverSpotDTO(
        String id,
        UserDTO user,
        String spotType,
        String spotName,
        BigDecimal lon,
        BigDecimal lat,
        Long measurementStationId,
        String riverName,
        Integer minOptimalWaterLevel,
        Integer maxOptimalWaterLevel
) implements SpotDTO {
}
