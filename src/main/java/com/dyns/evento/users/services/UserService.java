package com.dyns.evento.users.services;

import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.generics.AbstractService;
import com.dyns.evento.roles.RoleName;
import com.dyns.evento.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService extends AbstractService<User, UUID, UserRepository> {
    public UserService(UserRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public User save(User input) {
        try {
            input.setEnabled(true);
            input.setVerified(false);
            input.setRole(RoleName.ROLE_USER);
            return super.save(input);
        }
        catch(Exception exception) {
            log.info(exception.getCause().getMessage());
            throw exception;
        }
    }

    @Transactional
    public User partialUpdate(UUID uuid, User input) {
        return repository.findById(uuid)
                .map(updatedUser -> {
                    Optional.ofNullable(input.getEmail()).ifPresent(updatedUser::setEmail);
                    Optional.ofNullable(input.getAlias()).ifPresent(updatedUser::setAlias);
                    Optional.ofNullable(input.getFirstName()).ifPresent(updatedUser::setFirstName);
                    Optional.ofNullable(input.getLastName()).ifPresent(updatedUser::setLastName);
                    return repository.save(updatedUser);
                })
                .orElseThrow(() -> new NotFoundException(className));
    }

    @Transactional
    public User fullUpdate(UUID uuid, User input) {
        return repository.findById(uuid)
                .map(updatedUser -> {
                    updatedUser.setEmail(input.getEmail());
                    updatedUser.setAlias(input.getAlias());
                    updatedUser.setFirstName(input.getFirstName());
                    updatedUser.setLastName(input.getLastName());
                    return repository.save(updatedUser);
                })
                .orElseThrow(() -> new NotFoundException(className));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
