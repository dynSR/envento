package com.dyns.evento.registrations;

import com.dyns.evento.events.Event;
import com.dyns.evento.generic.Identifiable;
import com.dyns.evento.registrations.enums.RegistrationStatus;
import com.dyns.evento.registrations.utils.DefaultRegistration;
import com.dyns.evento.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "registrations"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "user_event", columnNames = {
//                        "user",
//                        "registration"
//                })
//        }
)
public class Registration implements Identifiable<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status = DefaultRegistration.STATUS;

    @NotNull
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt = DefaultRegistration.CREATED_AT;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private List<User> users = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REMOVE)
    private List<Event> events = new ArrayList<>();

    @Override
    public UUID getId() {
        return id;
    }
}
