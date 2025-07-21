package com.moscicki.user_service.validation;

import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.exception.UserServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserValidationTest {

    @Test
    void shouldPassWhenNameAndLastNameAreValid() {
        User user = new User(null, "john@example.com","John", "Doe");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldPassWithMixedCaseNames() {
        User user = new User(null, "jl@example.com", "Jean-Luc", "O'Neill");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldPassWithStandardEmail() {
        User user = new User(null, "john.doe@example.com", "John", "Doe");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldPassWithPlusInEmail() {
        User user = new User(null, "john+test@example.com", "John", "Doe");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldPassWithSubdomainEmail() {
        User user = new User(null, "john.doe@mail.example.com", "John", "Doe");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenNameIsNull() {
        User user = new User(null, null, "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        User user = new User(null, "   ", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenNameIsTooShort() {
        User user = new User(null, "J", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenNameHasInvalidCharacters() {
        User user = new User(null, "john@example.com", "J@hn!", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenLastNameIsNull() {
        User user = new User(null, "john@example.com", "John", null);
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenLastNameIsBlank() {
        User user = new User(null, "john@example.com", "John", "   ");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenLastNameIsTooShort() {
        User user = new User(null, "john@example.com", "John", "D");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenLastNameHasInvalidCharacters() {
        User user = new User(null, "john@example.com", "John", "Doe$");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailIsNull() {
        User user = new User(null, null, "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailIsBlank() {
        User user = new User(null, "   ", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailHasNoAtSymbol() {
        User user = new User(null, "johndoe.example.com", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailHasNoDomain() {
        User user = new User(null, "john@", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailHasSpaces() {
        User user = new User(null, "john doe@example.com", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailIsMissingUsername() {
        User user = new User(null, "@example.com", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailHasDoubleAt() {
        User user = new User(null, "john@@example.com", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldFailWhenEmailEndsWithDot() {
        User user = new User(null, "john.doe@example.com.", "John", "Doe");
        assertThrows(UserServiceException.class, () -> UserValidator.validate(user));
    }

}