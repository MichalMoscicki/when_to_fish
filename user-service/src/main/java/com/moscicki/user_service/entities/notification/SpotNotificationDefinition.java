package com.moscicki.user_service.entities.notification;


import com.moscicki.user_service.entities.BaseEntity;
import jakarta.persistence.*;


@Entity
public class SpotNotificationDefinition extends BaseEntity {

    private String spotId;
    private int executionHour;
    private int dayOfWeek;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    private NotificationType type;

    public SpotNotificationDefinition() {
    }

    public SpotNotificationDefinition(String id, String spotId, int hour, int dayOfWeek, NotificationType type) {
        this.setId(id);
        this.spotId = spotId;
        this.executionHour = hour;
        this.dayOfWeek = dayOfWeek;
        this.type = type;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getExecutionHour() {
        return executionHour;
    }

    public void setExecutionHour(int executionHour) {
        this.executionHour = executionHour;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
