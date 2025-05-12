package com.dyns.evento.users.services;

import com.dyns.evento.exceptions.NotFoundException;
import com.dyns.evento.generic.GenericService;
import com.dyns.evento.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements GenericService<User, UUID> {
    private final UserRepository repository;

    @Transactional
    @Override
    public User create(User input) {
        return repository.save(input);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<? extends User> getAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public User edit(UUID uuid, User changes) {
        return repository.findById(uuid)
                .map(foundUser -> {
                    Optional.ofNullable(changes.getEmail()).ifPresent(foundUser::setEmail);
                    Optional.ofNullable(changes.getFirstName()).ifPresent(foundUser::setFirstName);
                    Optional.ofNullable(changes.getLastName()).ifPresent(foundUser::setLastName);
                    Optional.ofNullable(changes.getPassword()).ifPresent(foundUser::setPassword);
                    Optional.ofNullable(changes.getRegistrations()).ifPresent(foundUser::setRegistrations);
                    Optional.ofNullable(changes.getEvents()).ifPresent(foundUser::setEvents);
                    return repository.save(foundUser);
                })
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public User update(UUID uuid, User update) {
        return repository.findById(uuid)
                .map(foundUser -> {
                    foundUser.setEmail(update.getEmail());
                    foundUser.setFirstName(update.getFirstName());
                    foundUser.setLastName(update.getLastName());
                    foundUser.setPassword(update.getPassword());
                    return repository.save(foundUser);
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
