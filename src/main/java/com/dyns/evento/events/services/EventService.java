package com.dyns.evento.events.services;

import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.events.Event;
import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.events.utils.EventValidationConstraints;
import com.dyns.evento.users.User;
import com.dyns.evento.users.services.UserRepository;
import com.dyns.evento.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public Event save(UUID userId, Event input) {
        validateEventDates(input);

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(User.class)));
        input.setCreator(creator);
        input.setStatus(EventStatus.DRAFT);
        return repository.save(input);
    }

    @Transactional(readOnly = true)
    public Event findById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(Event.class)));
    }

    @Transactional(readOnly = true)
    public Collection<? extends Event> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<? extends Event> findByCreator(
            UUID userId
    ) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(User.class)));
        return repository.findByCreator(creator);
    }

    @Transactional
    public Event partialUpdate(UUID uuid, Event input) {
        validateEventDates(input);
        return repository.findById(uuid)
                .map(foundEvent -> {
                    Optional.ofNullable(input.getTitle()).ifPresent(foundEvent::setTitle);
                    Optional.ofNullable(input.getDescription()).ifPresent(foundEvent::setDescription);
                    Optional.ofNullable(input.getStatus()).ifPresent(foundEvent::setStatus);
                    Optional.ofNullable(input.getMaxParticipants()).ifPresent(foundEvent::setMaxParticipants);
                    Optional.ofNullable(input.getStartingDateTime()).ifPresent(foundEvent::setStartingDateTime);
                    Optional.ofNullable(input.getEndingDateTime()).ifPresent(foundEvent::setEndingDateTime);
                    Optional.ofNullable(input.getRegistrationDeadline()).ifPresent(foundEvent::setRegistrationDeadline);
                    return repository.save(foundEvent);
                })
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(Event.class)));
    }

    @Transactional
    public Event fullUpdate(UUID uuid, Event input) {
        validateEventDates(input);
        return repository.findById(uuid)
                .map(foundEvent -> {
                    foundEvent.setTitle(input.getTitle());
                    foundEvent.setDescription(input.getDescription());
                    foundEvent.setStatus(input.getStatus());
                    foundEvent.setMaxParticipants(input.getMaxParticipants());
                    foundEvent.setStartingDateTime(input.getStartingDateTime());
                    foundEvent.setEndingDateTime(input.getEndingDateTime());
                    foundEvent.setRegistrationDeadline(input.getRegistrationDeadline());
                    return repository.save(foundEvent);
                })
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(Event.class)));
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.findById(uuid).ifPresentOrElse(
                repository::delete,
                () -> {
                    throw new NotFoundException(ClassUtils.getName(Event.class));
                }
        );
    }

    private void validateEventDates(Event input) {
        if (!input.getEndingDateTime().isAfter(input.getStartingDateTime())) {
            throw new IllegalArgumentException(EventValidationConstraints.INVALID_ENDING_DATE_MESSAGE);
        }

        if (input.getRegistrationDeadline().isBefore(input.getStartingDateTime())
                || input.getRegistrationDeadline().isAfter(input.getEndingDateTime())
        ) {
            throw new IllegalArgumentException(
                    EventValidationConstraints.INVALID_REGISTRATION_DEADLINE_MESSAGE
            );
        }
    }
}
