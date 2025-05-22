package com.dyns.evento.users.services;

import com.dyns.evento.users.User;
import com.dyns.evento.users.dtos.UserDto;
import com.dyns.evento.users.dtos.UserPatchRequest;
import com.dyns.evento.users.dtos.UserPostRequest;
import com.dyns.evento.users.dtos.UserPutRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${spring.application.api-base-url}")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @PostMapping("/users")
    private ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserPostRequest request
    ) {
        final User createdUser = service.save(mapper.toEntity(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(createdUser));
    }

    @GetMapping("/users/{id}")
    private ResponseEntity<UserDto> getUser(
            @PathVariable(name = "id") UUID id
    ) {
        return ResponseEntity.ok(mapper.toDto(service.findById(id)));
    }

    @GetMapping("/users")
    private ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @PatchMapping("/users/{id}")
    private ResponseEntity<UserDto> patchUser(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody UserPatchRequest request
    ) {
        final User patchedUser = service.partialUpdate(id, mapper.toEntity(request));
        return ResponseEntity.ok(mapper.toDto(patchedUser));

    }

    @PutMapping("/users/{id}")
    private ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody UserPutRequest request
    ) {
        final User updatedUser = service.fullUpdate(id, mapper.toEntity(request));
        return ResponseEntity.ok(mapper.toDto(updatedUser));
    }

    @DeleteMapping("/users/{id}")
    private ResponseEntity<Void> removeUser(
            @PathVariable(name = "id") UUID id
    ) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
