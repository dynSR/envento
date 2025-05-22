package com.dyns.evento.events.dtos;

import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.events.utils.EventValidationConstraints;
import com.dyns.evento.utils.GeneralConstraints;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventPutRequest {
    @NotBlank
    @Size(max = EventValidationConstraints.TITLE_MAX_LENGTH)
    private String title;

    @Pattern(
            regexp = GeneralConstraints.SAFE_TEXT_PATTERN,
            message = GeneralConstraints.INVALID_SAFE_TEXT_MESSAGE
    )
    private String description;

    @NotNull
    private EventStatus status;

    @NotNull
    @Min(EventValidationConstraints.MIN_PARTICIPANT)
    private Long maxParticipants;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startingDateTime;

    @NotNull
    @FutureOrPresent
    private LocalDateTime endingDateTime;

    @NotNull
    @FutureOrPresent
    private LocalDateTime registrationDeadline;
}
