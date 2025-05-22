package com.dyns.evento.registrations.services;

import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.events.Event;
import com.dyns.evento.events.services.EventRepository;
import com.dyns.evento.registrations.Registration;
import com.dyns.evento.registrations.enums.RegistrationStatus;
import com.dyns.evento.users.User;
import com.dyns.evento.users.services.UserRepository;
import com.dyns.evento.utils.ClassUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    // Maybe type more specifically the UUIDs to make sure they are respectively
    // UserUUID and EventUUID
    @Transactional
    public Registration save(
            @NotNull UUID userId,
            @NotNull UUID eventId
    ) {
        Registration registration = Registration.builder()
                .status(RegistrationStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(User.class)));
        registration.setUser(user);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(Event.class)));
        registration.setEvent(event);

        return repository.save(registration);
    }

    @Transactional(readOnly = true)
    public Registration findById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(Registration.class)));
    }

    @Transactional(readOnly = true)
    public Collection<? extends Registration> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.findById(uuid).ifPresentOrElse(
                repository::delete,
                () -> {
                    throw new NotFoundException(ClassUtils.getName(Registration.class));
                }
        );
    }
}
