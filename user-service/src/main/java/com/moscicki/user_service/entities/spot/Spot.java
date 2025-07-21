package com.moscicki.user_service.entities.spot;

import com.moscicki.user_service.entities.BaseEntity;
import com.moscicki.user_service.entities.user.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import java.math.BigDecimal;

@MappedSuperclass
public abstract class Spot extends BaseEntity {

    @ManyToOne
    private User user;
    private String spotName;
    private BigDecimal lon;
    private BigDecimal lat;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }
}
