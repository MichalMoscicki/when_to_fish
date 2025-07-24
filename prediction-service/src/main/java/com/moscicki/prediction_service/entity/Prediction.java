package com.moscicki.prediction_service.entity;

import jakarta.persistence.Entity;

@Entity
public class Prediction extends BaseEntity {
    private PredictionStatus status;
    private NotificationType type;
    private String spotId;
    private String userId;

    public PredictionStatus getStatus() {
        return status;
    }

    public void setStatus(PredictionStatus status) {
        this.status = status;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
