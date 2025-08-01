package com.dyns.evento.users;

import com.dyns.evento.users.utils.UserValidationConstraints;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
@ExtendWith(SpringExtension.class)
public class UserValidationTest {
    private final Validator validator;
    private final UserFixture fixture = new UserFixture();

    private Stream<Arguments> nullPropertySettersWithNames() {
        return java.util.stream.Stream.of(
                Arguments.of((Consumer<User>) u -> u.setEmail(null), "email", null),
                Arguments.of((Consumer<User>) u -> u.setFirstName(null), "firstName", null),
                Arguments.of((Consumer<User>) u -> u.setLastName(null), "lastName", null),
                Arguments.of((Consumer<User>) u -> u.setPassword(null), "password", null),
                Arguments.of((Consumer<User>) u -> u.setEmail(""), "email", ""),
                Arguments.of((Consumer<User>) u -> u.setFirstName(""), "firstName", ""),
                Arguments.of((Consumer<User>) u -> u.setLastName(""), "lastName", ""),
                Arguments.of((Consumer<User>) u -> u.setPassword(""), "password", "")
        );
    }

    public UserValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@missinglocal.com",
            ".dotstart@domain.com",
            "dotend.@domain.com",
            "double..dot@domain.com",
            "user name@domain.com",
            "user@domain_com.com",
            "user@domain.c",
            "user@domain.123",
            "user@-domain.com",
    })
    void shouldFailEmailValidation(String email) {
        // Given
        User user = fixture.getOne();
        user.setEmail(email);

        // When & Then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        log.info(violations.iterator().next().getMessage());
        assertFalse(violations.isEmpty());
        assertEquals(
                UserValidationConstraints.INVALID_EMAIL_MESSAGE,
                violations.iterator().next().getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short1A!",             // less than 12
            "aaaaaaaaaaaa",         // Char repetition 'a' > 2 times
            "123456789012",         // Only numbers, no caps or min or special chars
            "abcdefghijkL",         // only min + 1 caps, no number or special chars
            "ABCD12345678",         // caps + numbers, no min or special chars
            "aaaBBB111!!!",         // too short (11) + repetition
            "password1234",         // only min + numbers, no caps or special chars
            "PASSWORD!!!!!",        // only caps + special, no min or number chars
            "Passsword1234",        // triple 's' following
    })
    void shouldFailValidation_whenPasswordDoesNotMeetComplexityRequirements(String password) {
        //Given
        User user = fixture.getOne();
        user.setPassword(password);

        // When & Then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        log.info(violations.iterator().next().getMessage());
        assertFalse(violations.isEmpty());
        assertEquals(
                UserValidationConstraints.INVALID_PASSWORD_PATTERN_MESSAGE,
                violations.iterator().next().getMessage()
        );
    }

    @Test
    void shouldFailValidation_whenLastNameIsTooLong() {
        // Given
        User user = fixture.getOne();
        user.setLastName("a".repeat(UserValidationConstraints.LAST_NAME_MAX_LENGTH + 1));

        // When & Then
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        log.info(violations.iterator().next().getMessage());
        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("nullPropertySettersWithNames")
    void shouldFailValidation_whenPropertyIsNullOrBlank(
            Consumer<User> setter,
            String propertyName,
            Object assignedValue
    ) {
        // Given
        User user = fixture.getOne();
        setter.accept(user);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        log.info("{} assigned value: '{}', violation message: {}",
                propertyName,
                assignedValue,
                violations.iterator().next().getMessage()
        );
        assertFalse(violations.isEmpty());
        assertEquals(propertyName, violations.iterator().next().getPropertyPath().toString());
    }
}
