package com.moscicki.user_service.translator;

import com.moscicki.user_service.dto.user.UserDTO;
import com.moscicki.user_service.entities.user.User;

public class UserTranslator {

    public static UserDTO translate(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getLastName());

    }

    public static User translate(UserDTO userDTO) {
        return new User(userDTO.id(), userDTO.email(), userDTO.name(), userDTO.lastName());
    }
}
