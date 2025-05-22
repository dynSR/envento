package com.dyns.evento.events;

import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.generics.Fixture;
import com.dyns.evento.users.User;
import com.dyns.evento.users.UserFixture;

import java.time.LocalDateTime;
import java.util.*;

public class EventFixture implements Fixture<Event, UUID> {
    private final List<Event> events = new ArrayList<>();

    @Override
    public Event getOne() {
        return Event.builder()
                .id(null)
                .title("Music festival")
                .description("Music festival in Bordeaux.")
                .status(EventStatus.DRAFT)
                .maxParticipants(100L)
                .startingDateTime(LocalDateTime.now().plusDays(1))
                .endingDateTime(LocalDateTime.now().plusDays(5))
                .registrationDeadline(LocalDateTime.now().plusDays(3))
                .registrations(new HashSet<>())
                .build();
    }

    public Event getOneWithCreator(User creator) {
        return Event.builder()
                .id(null)
                .title("Music festival")
                .description("Music festival in Bordeaux.")
                .status(EventStatus.DRAFT)
                .maxParticipants(100L)
                .startingDateTime(LocalDateTime.now().plusDays(1))
                .endingDateTime(LocalDateTime.now().plusDays(5))
                .registrationDeadline(LocalDateTime.now().plusDays(3))
                .creator(creator)
                .registrations(new HashSet<>())
                .build();
    }

    @Override
    public Collection<? extends Event> getMany() {
        User creator = new UserFixture().getOne();
        for (int i = 0; i < getFixtureAmount(); i++) {
            events.add(Event.builder()
                    .id(null)
                    .title("Event " + i)
                    .description("Event description")
                    .status(EventStatus.DRAFT)
                    .maxParticipants(100L)
                    .startingDateTime(LocalDateTime.now().plusDays(1))
                    .endingDateTime(LocalDateTime.now().plusDays(5))
                    .registrationDeadline(LocalDateTime.now().plusDays(3))
                    .creator(creator)
                    .registrations(new HashSet<>())
                    .build()
            );
        }
        return events;
    }

    @Override
    public int getFixtureAmount() {
        return 5;
    }
}
