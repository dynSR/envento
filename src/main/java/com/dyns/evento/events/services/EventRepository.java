package com.dyns.evento.events.services;

import com.dyns.evento.events.Event;
import com.dyns.evento.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Collection<? extends Event> findByCreator(User creator);
}
