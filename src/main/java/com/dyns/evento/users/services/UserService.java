package com.dyns.evento.users.services;

import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.users.User;
import com.dyns.evento.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    @Transactional
    public User save(User input) {
        return repository.save(input);
    }

    @Transactional(readOnly = true)
    public User findById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(User.class)));
    }

    @Transactional(readOnly = true)
    public Collection<? extends User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public User partialUpdate(UUID uuid, User input) {
        return repository.findById(uuid)
                .map(updatedUser -> {
                    Optional.ofNullable(input.getEmail()).ifPresent(updatedUser::setEmail);
                    Optional.ofNullable(input.getFirstName()).ifPresent(updatedUser::setFirstName);
                    Optional.ofNullable(input.getLastName()).ifPresent(updatedUser::setLastName);
                    Optional.ofNullable(input.getRegistrations()).ifPresent(updatedUser::setRegistrations);
                    Optional.ofNullable(input.getEvents()).ifPresent(updatedUser::setEvents);
                    return repository.save(updatedUser);
                })
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(User.class)));
    }

    @Transactional
    public User fullUpdate(UUID uuid, User input) {
        return repository.findById(uuid)
                .map(updatedUser -> {
                    updatedUser.setEmail(input.getEmail());
                    updatedUser.setFirstName(input.getFirstName());
                    updatedUser.setLastName(input.getLastName());
                    return repository.save(updatedUser);
                })
                .orElseThrow(() -> new NotFoundException(ClassUtils.getName(User.class)));
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.findById(uuid).ifPresentOrElse(
                repository::delete,
                () -> {
                    throw new NotFoundException(ClassUtils.getName(User.class));
                }
        );
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
