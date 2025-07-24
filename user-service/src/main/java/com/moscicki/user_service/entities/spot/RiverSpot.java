package com.moscicki.user_service.entities.spot;

import com.moscicki.user_service.dto.spot.SpotType;
import jakarta.persistence.Entity;

@Entity
public class RiverSpot extends Spot {

    private Long measurementStationId;
    private String riverName;
    private Integer minOptimalWaterLevel;
    private Integer maxOptimalWaterLevel;

    public Long getMeasurementStationId() {
        return measurementStationId;
    }

    public void setMeasurementStationId(Long measurementStationId) {
        this.measurementStationId = measurementStationId;
    }

    public String getRiverName() {
        return riverName;
    }

    public void setRiverName(String riverName) {
        this.riverName = riverName;
    }

    public Integer getMinOptimalWaterLevel() {
        return minOptimalWaterLevel;
    }

    public void setMinOptimalWaterLevel(Integer minOptimalWaterLevel) {
        this.minOptimalWaterLevel = minOptimalWaterLevel;
    }

    public Integer getMaxOptimalWaterLevel() {
        return maxOptimalWaterLevel;
    }

    public void setMaxOptimalWaterLevel(Integer maxOptimalWaterLevel) {
        this.maxOptimalWaterLevel = maxOptimalWaterLevel;
    }

    @Override
    public String getSpotType() {
        return SpotType.RIVER_SPOT;
    }
}
