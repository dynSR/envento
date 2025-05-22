package com.dyns.evento.events;

import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.events.utils.EventValidationConstraints;
import com.dyns.evento.generics.Identifiable;
import com.dyns.evento.registrations.Registration;
import com.dyns.evento.users.User;
import com.dyns.evento.utils.GeneralConstraints;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event implements Identifiable<UUID> {
    @PreRemove
    private void onPreRemove() {
//        setCreator(null);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @NaturalId(mutable = true)
    @NotBlank
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
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private EventStatus status;

    @NotNull
    @Min(EventValidationConstraints.MIN_PARTICIPANT)
    @Column(nullable = false)
    private Long maxParticipants;

    @NotNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime startingDateTime;

    @NotNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime endingDateTime;

    @NotNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime registrationDeadline;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private Set<Registration> registrations = new HashSet<>();

    @Override
    public UUID getId() {
        return id;
    }

    public void setCreator(User creator) {
        if (creator == null) {
            this.creator.removeEvent(this);
            this.creator = null;
            return;
        }

        this.creator = creator;
        creator.addEvent(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id)
                && Objects.equals(title, event.title)
                && Objects.equals(description, event.description)
                && status == event.status
                && Objects.equals(maxParticipants, event.maxParticipants)
                && Objects.equals(startingDateTime, event.startingDateTime)
                && Objects.equals(endingDateTime, event.endingDateTime)
                && Objects.equals(registrationDeadline, event.registrationDeadline)
                && Objects.equals(creator, event.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                title,
                description,
                status,
                maxParticipants,
                startingDateTime,
                endingDateTime,
                registrationDeadline,
                creator
        );
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", maxParticipants=" + maxParticipants +
                ", startingDateTime=" + startingDateTime +
                ", endingDateTime=" + endingDateTime +
                ", registrationDeadline=" + registrationDeadline +
                ", creator=" + creator +
                '}';
    }

    public void addRegistration(Registration registration) {
        if (registrations.contains(registration)) return;
        registrations.add(registration);
    }

    public void removeRegistration(Registration registration) {
        if (!registrations.contains(registration)) return;
        registrations.removeIf(r -> r.getId().equals(registration.getId()));
    }

    public void batchRemoveRegistration(Collection<Registration> batch) {
        registrations.removeAll(batch);
    }

    public void clearRegistrations() {
        if (registrations.isEmpty()) return;
        registrations.clear();
    }
}
