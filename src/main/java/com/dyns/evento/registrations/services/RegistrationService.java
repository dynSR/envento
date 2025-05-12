package com.dyns.evento.registrations.services;

import com.dyns.evento.exceptions.NotFoundException;
import com.dyns.evento.generic.GenericService;
import com.dyns.evento.registrations.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService implements GenericService<Registration, UUID> {
    private final RegistrationRepository repository;

    @Transactional
    @Override
    public Registration create(Registration input) {
        return repository.save(input);
    }

    @Transactional(readOnly = true)
    @Override
    public Registration getById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<? extends Registration> getAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public Registration edit(UUID uuid, Registration changes) {
        return repository.findById(uuid)
                .map(registration -> {
                    Optional.ofNullable(changes.getStatus()).ifPresent(registration::setStatus);
                    Optional.ofNullable(changes.getCreatedAt()).ifPresent(registration::setCreatedAt);
                    return repository.save(registration);
                })
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public Registration update(UUID uuid, Registration update) {
        return repository.findById(uuid)
                .map(registration -> {
                    registration.setStatus(update.getStatus());
                    registration.setCreatedAt(update.getCreatedAt());
                    return repository.save(registration);
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
