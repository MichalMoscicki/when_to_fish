package com.moscicki.user_service.validation;

import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.exception.UserServiceException;

import java.util.Objects;

public class AccessValidator {

    public static void validate(User user, Spot spot) {
        if (!Objects.equals(user.getId(), spot.getUser().getId())) {
            throw new UserServiceException("User do not match spot");
        }
    }
}
