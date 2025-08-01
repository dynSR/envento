package com.dyns.evento.events.services;

import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.events.Event;
import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.events.utils.EventValidationConstraints;
import com.dyns.evento.generics.AbstractService;
import com.dyns.evento.users.User;
import com.dyns.evento.users.services.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EventService extends AbstractService<Event, UUID, EventRepository> {
    private final UserRepository userRepository;

    public EventService(
            EventRepository repository,
            UserRepository userRepository
    ) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Transactional
    public Event save(
            UUID userId,
            Event input
    ) {
        EventValidationConstraints.validateEventDates(input);

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(className));
        input.setCreator(creator);
        input.setStatus(EventStatus.DRAFT);
        return repository.save(input);
    }

    @Transactional(readOnly = true)
    public Collection<? extends Event> findByCreator(UUID userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(className));
        return repository.findByCreator(creator);
    }

    @Override
    public Event partialUpdate(
            UUID uuid,
            Event input
    ) {
        EventValidationConstraints.validateEventDates(input);
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
                .orElseThrow(() -> new NotFoundException(className));
    }

    @Override
    public Event fullUpdate(UUID uuid, Event input) {
        EventValidationConstraints.validateEventDates(input);
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
                .orElseThrow(() -> new NotFoundException(className));
    }
}
