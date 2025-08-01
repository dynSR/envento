package com.dyns.evento.events;

import com.dyns.evento.events.utils.EventValidationConstraints;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
@ExtendWith(SpringExtension.class)
public class EventValidationTest {
    private final Validator validator;
    private final EventFixture fixture = new EventFixture();

    private Stream<Arguments> nullPropertySettersWithNames() {
        return Stream.of(
                Arguments.of((Consumer<Event>) e ->
                        e.setTitle(null), "title"),
                Arguments.of((Consumer<Event>) e ->
                        e.setStatus(null), "status"),
                Arguments.of((Consumer<Event>) e ->
                        e.setMaxParticipants(null), "maxParticipants"),
                Arguments.of((Consumer<Event>) e ->
                        e.setStartingDateTime(null), "startingDateTime"),
                Arguments.of((Consumer<Event>) e ->
                        e.setEndingDateTime(null), "endingDateTime"),
                Arguments.of((Consumer<Event>) e ->
                        e.setRegistrationDeadline(null), "registrationDeadline"),
                Arguments.of((Consumer<Event>) e ->
                        e.setCreator(null), "creator"),
                Arguments.of((Consumer<Event>) e ->
                        e.setTitle(""), "title")
        );
    }

    private Stream<Arguments> invalidDateSettersWithNames() {
        return Stream.of(
                Arguments.of((Consumer<Event>) e ->
                        e.setStartingDateTime(LocalDateTime.now().minusDays(1)), "startingDateTime"),
                Arguments.of((Consumer<Event>) e ->
                        e.setEndingDateTime(LocalDateTime.now().minusDays(1)), "endingDateTime"),
                Arguments.of((Consumer<Event>) e ->
                        e.setRegistrationDeadline(LocalDateTime.now().minusDays(1)), "registrationDeadline")
        );
    }

    public EventValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidation_whenTitleExceedsMaxLength() {
        // Given
        Event event = fixture.getOne();
        event.setTitle("A".repeat(EventValidationConstraints.TITLE_MAX_LENGTH + 1));

        // When
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Then
        log.info(violations.iterator().next().getMessage());
        assertFalse(violations.isEmpty());
        assertEquals(
                "title",
                violations.iterator().next().getPropertyPath().toString()
        );
    }

    @Test
    void shouldFailValidation_whenMaxParticipantIsLesserThanZero() {
        // Given
        Event event = fixture.getOne();
        event.setMaxParticipants(-1L);

        // When
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Then
        log.info(violations.iterator().next().getMessage());
        assertFalse(violations.isEmpty());
        assertEquals(
                "maxParticipants",
                violations.iterator().next().getPropertyPath().toString()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDateSettersWithNames")
    void shouldFailValidation_whenDateIsInThePast(
            Consumer<Event> dateSetter,
            String propertyName
    ) {
        // Given
        Event event = fixture.getOne();
        dateSetter.accept(event);

        // When
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Then
        log.info("{}: {}", propertyName, violations.iterator().next().getMessage());
        assertFalse(violations.isEmpty());
        assertEquals(propertyName, violations.iterator().next().getPropertyPath().toString());
    }

    @ParameterizedTest
    @MethodSource("nullPropertySettersWithNames")
    void shouldFailValidation_whenPropertyIsNullOrBlank(
            Consumer<Event> setter,
            String propertyName
    ) {
        // Given
        Event event = fixture.getOne();
        setter.accept(event);

        // When
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Then
        log.info("{}: {}", propertyName, violations.iterator().next().getMessage());
        assertFalse(violations.isEmpty());
        assertEquals(propertyName, violations.iterator().next().getPropertyPath().toString());
    }
}
