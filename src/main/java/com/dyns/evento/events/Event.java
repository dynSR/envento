package com.dyns.evento.events;

import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.events.utils.DefaultEvent;
import com.dyns.evento.events.utils.EventConstants;
import com.dyns.evento.generic.Identifiable;
import com.dyns.evento.registrations.Registration;
import com.dyns.evento.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event implements Identifiable<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @NaturalId
    @NotBlank
    @Size(max = EventConstants.TITLE_MAX_LENGTH)
    @Column(
            length = EventConstants.TITLE_MAX_LENGTH,
            nullable = false,
            unique = true
    )
    private String title = DefaultEvent.TITLE;

    @Column(columnDefinition = "TEXT")
    private String description = DefaultEvent.DESCRIPTION;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private EventStatus status = DefaultEvent.STATUS;

    @NotNull
    @Min(EventConstants.MIN_PARTICIPANT)
    @Column(nullable = false)
    private Long maxParticipants = DefaultEvent.MAX_PARTICIPANTS;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startingDateTime = DefaultEvent.STARTING_DATE_TIME;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endingDateTime = DefaultEvent.ENDING_DATE_TIME;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime registrationDeadline = DefaultEvent.REGISTRATION_DEADLINE;

    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = User.class)
    private User createdBy;

    @OneToMany
    private Set<Registration> registrations = new HashSet<>();

    @Override
    public UUID getId() {
        return id;
    }
}
