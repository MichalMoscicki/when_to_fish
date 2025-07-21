package com.moscicki.user_service.validation;

import com.moscicki.user_service.exception.UserServiceException;

public class IdValidator {

    private final static String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";


    public static void validate(String id) {
        if (id == null) {
            return;
        }
        if (!id.matches(UUID_REGEX)) {
            throw new UserServiceException("Invalid id: " + id);
        }
    }

}
