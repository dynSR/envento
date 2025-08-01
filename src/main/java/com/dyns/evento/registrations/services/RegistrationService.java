package com.dyns.evento.registrations.services;

import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.events.Event;
import com.dyns.evento.events.services.EventRepository;
import com.dyns.evento.generics.AbstractService;
import com.dyns.evento.registrations.Registration;
import com.dyns.evento.registrations.enums.RegistrationStatus;
import com.dyns.evento.users.User;
import com.dyns.evento.users.services.UserRepository;
import com.dyns.evento.utils.ClassUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService extends AbstractService<Registration, UUID, RegistrationRepository> {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RegistrationService(
            RegistrationRepository repository,
            UserRepository userRepository,
            EventRepository eventRepository
    ) {
        super(repository);
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

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

        return super.save(registration);
    }

    @Override
    public Registration partialUpdate(UUID uuid, Registration input) {
        return fullUpdate(uuid, input);
    }

    @Override
    public Registration fullUpdate(UUID uuid, Registration input) {
        return repository.findById(uuid)
                .map(updatedRegistration -> {
                    updatedRegistration.setStatus(input.getStatus());
                    return repository.save(updatedRegistration);
                })
                .orElseThrow(() -> new NotFoundException(className));
    }
}
