package com.dyns.evento.events.services;

import com.dyns.evento.events.Event;
import com.dyns.evento.exceptions.NotFoundException;
import com.dyns.evento.generic.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService implements GenericService<Event, UUID> {
    private final EventRepository repository;

    @Transactional
    @Override
    public Event create(Event input) {
        return repository.save(input);
    }

    @Transactional(readOnly = true)
    @Override
    public Event getById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<? extends Event> getAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public Event edit(UUID uuid, Event changes) {
        return repository.findById(uuid)
                .map(foundEvent -> {
                    Optional.ofNullable(changes.getTitle()).ifPresent(foundEvent::setTitle);
                    Optional.ofNullable(changes.getDescription()).ifPresent(foundEvent::setDescription);
                    Optional.ofNullable(changes.getStatus()).ifPresent(foundEvent::setStatus);
                    Optional.ofNullable(changes.getMaxParticipants()).ifPresent(foundEvent::setMaxParticipants);
                    Optional.ofNullable(changes.getStartingDateTime()).ifPresent(foundEvent::setStartingDateTime);
                    Optional.ofNullable(changes.getEndingDateTime()).ifPresent(foundEvent::setEndingDateTime);
                    Optional.ofNullable(changes.getRegistrationDeadline()).ifPresent(foundEvent::setRegistrationDeadline);
                    return repository.save(foundEvent);
                })
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public Event update(UUID uuid, Event update) {
        return repository.findById(uuid)
                .map(foundEvent -> {
                    foundEvent.setTitle(update.getTitle());
                    foundEvent.setDescription(update.getDescription());
                    foundEvent.setStatus(update.getStatus());
                    foundEvent.setMaxParticipants(update.getMaxParticipants());
                    foundEvent.setStartingDateTime(update.getStartingDateTime());
                    foundEvent.setEndingDateTime(update.getEndingDateTime());
                    foundEvent.setRegistrationDeadline(update.getRegistrationDeadline());
                    return repository.save(foundEvent);
                })
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public void delete(UUID uuid) {
        repository.findById(uuid).ifPresentOrElse(
                repository::delete,
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
