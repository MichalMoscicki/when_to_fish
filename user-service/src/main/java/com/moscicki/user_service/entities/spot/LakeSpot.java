package com.moscicki.user_service.entities.spot;

import com.moscicki.user_service.entities.user.User;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class LakeSpot extends Spot {

    private String lakeName;

    public LakeSpot() {
    }

    public LakeSpot(User user, String spotName, BigDecimal lon, BigDecimal lat, String lakeName) {
        super();
    }

    public String getLakeName() {
        return lakeName;
    }

    public void setLakeName(String lakeName) {
        this.lakeName = lakeName;
    }
}
