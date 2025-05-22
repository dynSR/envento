package com.dyns.evento.events.dtos;

import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.events.utils.EventValidationConstraints;
import com.dyns.evento.utils.GeneralConstraints;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventPatchRequest {
    @Size(max = EventValidationConstraints.TITLE_MAX_LENGTH)
    @Column(
            length = EventValidationConstraints.TITLE_MAX_LENGTH,
            nullable = false,
            unique = true
    )
    private String title;

    @Pattern(
            regexp = GeneralConstraints.SAFE_TEXT_PATTERN,
            message = GeneralConstraints.INVALID_SAFE_TEXT_MESSAGE
    )
    private String description;

    private EventStatus status;

    @Min(EventValidationConstraints.MIN_PARTICIPANT)
    private Long maxParticipants;

    @FutureOrPresent
    private LocalDateTime startingDateTime;

    @FutureOrPresent
    private LocalDateTime endingDateTime;

    @FutureOrPresent
    private LocalDateTime registrationDeadline;
}
