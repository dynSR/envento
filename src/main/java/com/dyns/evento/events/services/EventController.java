package com.dyns.evento.events.services;

import com.dyns.evento.events.Event;
import com.dyns.evento.events.dtos.EventDto;
import com.dyns.evento.events.dtos.EventPatchRequest;
import com.dyns.evento.events.dtos.EventPostRequest;
import com.dyns.evento.events.dtos.EventPutRequest;
import com.dyns.evento.events.enums.EventStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${spring.application.api-base-url}")
@RequiredArgsConstructor
public class EventController {
    private final EventService service;
    private final EventMapper mapper;

    @PostMapping("/users/{userId}" + "/events")
    private ResponseEntity<EventDto> Event(
            @PathVariable(name = "userId") UUID userId,
            @Valid @RequestBody EventPostRequest request
    ) {
        final Event createdEvent = service.save(
                userId,
                mapper.toEntity(request)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(createdEvent));
    }

    @GetMapping("/events/{id}")
    private ResponseEntity<EventDto> getEvent(
            @PathVariable(name = "id") UUID id
    ) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @GetMapping("/events")
    private ResponseEntity<List<EventDto>> getEvents() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @GetMapping(
            "/users/{userId}"
                    + "/events"
    )
    private ResponseEntity<List<EventDto>> getEvents(
            @PathVariable(name = "userId") UUID userId
    ) {
        return ResponseEntity.ok(
                service.findByCreator(userId).stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @PatchMapping("/events/{id}")
    private ResponseEntity<EventDto> patchEvent(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody EventPatchRequest request
    ) {
        if (request.getStatus() != null
                && !ObjectUtils.containsConstant(EventStatus.values(), request.getStatus().name(), true))
            throw new RuntimeException("test");
        Event patchedEvent = service.partialUpdate(id, mapper.toEntity(request));
        return ResponseEntity.ok(mapper.toDto(patchedEvent));
    }

    @PutMapping("/events/{id}")
    private ResponseEntity<EventDto> updateEvent(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody EventPutRequest request
    ) {
        Event updatedEvent = service.fullUpdate(id, mapper.toEntity(request));
        return ResponseEntity.ok(mapper.toDto(updatedEvent));
    }

    @DeleteMapping("/events/{id}")
    private ResponseEntity<Void> removeEvent(
            @PathVariable(name = "id") UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
