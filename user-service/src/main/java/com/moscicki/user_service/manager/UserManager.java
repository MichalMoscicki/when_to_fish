package com.moscicki.user_service.manager;

import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.exception.UserServiceException;
import com.moscicki.user_service.repository.UserRepository;
import com.moscicki.user_service.validation.IdValidator;
import com.moscicki.user_service.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManager {
    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        UserValidator.validate(user);
        checkIfAlreadyExist(user);
        return userRepository.save(user);
    }

    private void checkIfAlreadyExist(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserServiceException("User with given email already exists");
        }
    }

    public User getUser(String id) {
        IdValidator.validate(id);
        return userRepository.findById(id).orElseThrow(() -> new UserServiceException("No user with given id: " + id));
    }

    public User updateUser(String id, User userParam) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserServiceException("No user with given id: " + id));
        UserValidator.validateEdit(user, userParam);
        mergeUsers(user, userParam);
        return userRepository.save(user);
    }

    private void mergeUsers(User userBeforeEdit, User userAfterEdit) {
        userBeforeEdit.setName(userAfterEdit.getName());
        userBeforeEdit.setLastName(userAfterEdit.getLastName());
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
