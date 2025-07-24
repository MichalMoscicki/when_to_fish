package com.moscicki.user_service.translator;

import com.moscicki.user_service.dto.spot.LakeSpotDTO;
import com.moscicki.user_service.dto.spot.RiverSpotDTO;
import com.moscicki.user_service.dto.spot.SpotDTO;
import com.moscicki.user_service.dto.spot.SpotType;
import com.moscicki.user_service.entities.spot.LakeSpot;
import com.moscicki.user_service.entities.spot.RiverSpot;
import com.moscicki.user_service.entities.spot.Spot;

import java.util.List;

public class SpotTranslator {

    public static SpotDTO translate(Spot spot) {
        switch (spot.getSpotType()) {
            case SpotType.RIVER_SPOT -> {
                return new RiverSpotDTO(spot.getId(), UserTranslator.translate(spot.getUser()), SpotType.RIVER_SPOT, spot.getSpotName(), spot.getLon(), spot.getLat(), ((RiverSpot) spot).getMeasurementStationId(), ((RiverSpot) spot).getRiverName(), ((RiverSpot) spot).getMinOptimalWaterLevel(), ((RiverSpot) spot).getMaxOptimalWaterLevel());
            }
            case SpotType.LAKE_SPOT -> {
                return new LakeSpotDTO(spot.getId(), UserTranslator.translate(spot.getUser()), SpotType.LAKE_SPOT, spot.getSpotName(), spot.getLon(), spot.getLat(), ((LakeSpot) spot).getLakeName());
            }
            default -> {throw new IllegalArgumentException("Unknown spot subclass: " + spot.getClass());}
        }
    }

    public static List<SpotDTO> translateList(List<Spot> spots) {
        return spots.stream().map(SpotTranslator::translate).toList();
    }

    public static List<Spot> translateDTOList(List<SpotDTO> spotDTOS) {
        return spotDTOS.stream().map(SpotTranslator::translate).toList();
    }

    public static Spot translate(SpotDTO spotDTO) {
        if (spotDTO instanceof RiverSpotDTO riverDto) {
            return translate(riverDto);
        }
        if (spotDTO instanceof LakeSpotDTO lakeDto) {
            return translate(lakeDto);
        }
        throw new IllegalArgumentException("Unknown spotDTO subclass: " + spotDTO.getClass());
    }

    private static RiverSpot translate(RiverSpotDTO dto) {
        RiverSpot spot = new RiverSpot();
        setCommonSpotFields(spot, dto);
        spot.setMeasurementStationId(dto.measurementStationId());
        spot.setRiverName(dto.riverName());
        spot.setMinOptimalWaterLevel(dto.minOptimalWaterLevel());
        spot.setMaxOptimalWaterLevel(dto.maxOptimalWaterLevel());
        return spot;
    }

    private static LakeSpot translate(LakeSpotDTO dto) {
        LakeSpot spot = new LakeSpot();
        setCommonSpotFields(spot, dto);
        spot.setLakeName(dto.lakeName());
        return spot;
    }

    private static void setCommonSpotFields(Spot spot, SpotDTO dto) {
        if (dto.user() != null) {
            spot.setUser(UserTranslator.translate(dto.user()));
        }
        spot.setId(dto.id());
        spot.setSpotName(dto.spotName());
        spot.setLat(dto.lat());
        spot.setLon(dto.lon());
    }


}
