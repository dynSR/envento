package com.dyns.evento.registrations.services;

import com.dyns.evento.registrations.Registration;
import com.dyns.evento.registrations.dtos.RegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${spring.application.api-base-url}")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService service;
    private final RegistrationMapper mapper;

    @PostMapping(
            "/users/{userId}"
                    + "/events/{eventId}"
                    + "/registrations"
    )
    private ResponseEntity<RegistrationDto> createRegistration(
            @PathVariable(name = "userId") UUID userId,
            @PathVariable(name = "eventId") UUID eventId
    ) {
        final Registration createdRegistration = service.save(userId, eventId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(createdRegistration));
    }

    @GetMapping("/{id}")
    private ResponseEntity<RegistrationDto> getRegistration(
            @PathVariable(name = "id") UUID id
    ) {
        return ResponseEntity
                .ok(mapper.toDto(service.findById(id)));
    }

    @GetMapping
    private ResponseEntity<List<RegistrationDto>> getRegistrations() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @DeleteMapping("/registrations/{id}")
    private ResponseEntity<Void> removeRegistration(
            @PathVariable(name = "id") UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
