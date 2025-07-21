package com.moscicki.user_service.validation;

import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.exception.UserServiceException;

import java.util.Objects;

public class UserValidator {

    public static void validateEdit(User user, User userParam) {
        if (!Objects.equals(user.getEmail(), userParam.getEmail())) {
            throw new UserServiceException("Email can not be edited");
        }
        validate(userParam);
    }

    public static void validate(User user) {
        IdValidator.validate(user.getId());
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new UserServiceException("Email can not be null or empty");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new UserServiceException("Name can not be null or empty");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new UserServiceException("LastName can not be null or empty");
        }
        if (user.getName().length() < 2) {
            throw new UserServiceException("Name is too short");
        }
        if (user.getLastName().length() < 2) {
            throw new UserServiceException("LastName is too short");
        }
        String emailRegex = "^[\\w.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new UserServiceException("Email incorrect");
        }
        String nameRegex = "^[A-Za-z]+([ '-][A-Za-z]+)*$";
        if (!user.getName().matches(nameRegex)) {
            throw new UserServiceException("Name incorrect");
        }
        if (!user.getLastName().matches(nameRegex)) {
            throw new UserServiceException("LastName incorrect");
        }
    }

}
