package com.moscicki.user_service.dto.spot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.moscicki.user_service.dto.user.UserDTO;

import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LakeSpotDTO.class, name = SpotType.LAKE_SPOT),
        @JsonSubTypes.Type(value = RiverSpotDTO.class, name = SpotType.RIVER_SPOT)
})
public interface SpotDTO {
    String id();

    UserDTO user();

    String spotName();

    String spotType();

    BigDecimal lon();

    BigDecimal lat();
}