package com.dyns.evento.events;

import com.dyns.evento.events.enums.EventStatus;
import com.dyns.evento.events.services.EventRepository;
import com.dyns.evento.generics.AbstractRepositoryIT;
import com.dyns.evento.users.User;
import com.dyns.evento.users.UserFixture;
import com.dyns.evento.users.services.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class EventRepositoryIT extends AbstractRepositoryIT<Event, UUID, EventRepository, EventFixture> {
    private final UserRepository userRepository;
    private final UserFixture userFixture = new UserFixture();

    @Autowired
    private EventRepositoryIT(
            EventRepository underTest,
            UserRepository userRepository
    ) {
        super(underTest);
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Should only partially update an event")
    @Override
    protected void shouldPartiallyUpdateEntity() {
        // Given
        Event savedEvent = getSavedEntity();
        log.info("Saved event: {}", savedEvent);

        // When & Then
        underTest.findById(savedEvent.getId()).ifPresent(
                e -> {
                    getUpdateModifiers(true).forEach(
                            modifier -> modifier.accept(e)
                    );

                    Event updatedEvent = underTest.save(e);
                    log.info("Updated event: {}", updatedEvent);
                    assertEquals(
                            e.getTitle(),
                            updatedEvent.getTitle()
                    );
                    assertEquals(
                            e.getDescription(),
                            updatedEvent.getDescription()
                    );
                }
        );
    }

    @Test
    @DisplayName("Should only partially update an event")
    @Override
    protected void shouldFullyUpdateEntity() {
        // Given
        Event savedEvent = getSavedEntity();

        // When & Then
        underTest.findById(savedEvent.getId()).ifPresent(
                e -> {
                    getUpdateModifiers(false)
                            .forEach(modifier -> modifier.accept(e));

                    Event updatedEvent = underTest.save(e);
                    assertEquals(
                            e.getTitle(),
                            updatedEvent.getTitle()
                    );
                    assertEquals(
                            e.getDescription(),
                            updatedEvent.getDescription()
                    );
                    assertEquals(
                            e.getStatus(),
                            updatedEvent.getStatus()
                    );
                    assertEquals(
                            e.getMaxParticipants(),
                            updatedEvent.getMaxParticipants()
                    );
                    assertEquals(
                            e.getStartingDateTime(),
                            updatedEvent.getStartingDateTime()
                    );
                    assertEquals(
                            e.getEndingDateTime(),
                            updatedEvent.getEndingDateTime()
                    );
                    assertEquals(
                            e.getRegistrationDeadline(),
                            updatedEvent.getRegistrationDeadline()
                    );
                }
        );
    }

    @Test
    @DisplayName("Should return all events of a specific creator")
    void shouldReturnOnlyEventsOfCreator_whenFindingByCreator() {
        // Given
        User john = getSavedCreator();

        User jane = userFixture.getOne();
        jane.setEmail("jane.due@domain.fr");
        jane.setFirstName("Jane");
        userRepository.save(jane);

        Event musicFest = getFixture().getOne();
        musicFest.setTitle("Music Fest");
        musicFest.setCreator(john);
        underTest.save(musicFest);

        Event computerContest = getFixture().getOne();
        computerContest.setTitle("Computer Contest");
        computerContest.setCreator(john);
        underTest.save(computerContest);

        Event videoGameTournament = getFixture().getOne();
        videoGameTournament.setTitle("CoD LAN Tournament");
        videoGameTournament.setCreator(jane);
        underTest.save(videoGameTournament);

        // When
        Collection<? extends Event> johnEvents = underTest.findByCreator(john);
        Collection<? extends Event> janeEvents = underTest.findByCreator(jane);

        // Then
        log.info(
                "All events : {}",
                underTest.findAll().stream().map(Event::getTitle).collect(Collectors.toList())
        );
        log.info(
                "Event for {}: {}",
                john.getFirstName(),
                johnEvents.stream().map(Event::getTitle).collect(Collectors.toList())
        );
        log.info(
                "Event for {}: {}",
                jane.getFirstName(),
                janeEvents.stream().map(Event::getTitle).collect(Collectors.toList())
        );
        assertNotNull(johnEvents);
        assertEquals(2, johnEvents.size());
        assertEquals(1, janeEvents.size());
        assertTrue(
                johnEvents.stream()
                        .allMatch(e -> e.getCreator().equals(john))
        );
        assertTrue(
                janeEvents.stream()
                        .allMatch(e -> e.getCreator().equals(jane))
        );
    }

    @Override
    protected EventFixture getFixture() {
        return new EventFixture();
    }

    @Override
    protected Event getSavedEntity() {
        Event event = getFixture().getOne();
        event.setCreator(getSavedCreator());
        return underTest.save(event);
    }

    @Override
    protected void saveOneEntity() {
        Event event = getFixture().getOne();
        event.setCreator(getSavedCreator());
        underTest.save(event);
    }

    @Override
    protected void saveEntities() {
        User creator = getSavedCreator();
        getFixture()
                .getMany()
                .forEach(e -> {
                    e.setCreator(creator);
                    underTest.save(e);
                });
    }

    /**
     * Update full object or only title and description
     * @param isPartialUpdate
     * @return a set of modifier to apply to an event
     */
    @Override
    protected Stream<Consumer<Event>> getUpdateModifiers(boolean isPartialUpdate) {
        if (isPartialUpdate)
            return Stream.of(
                    e -> e.setTitle("New Title"),
                    e -> e.setDescription("New Description")
            );

        return Stream.of(
                e -> e.setTitle("New Title"),
                e -> e.setDescription("New Description"),
                e -> e.setStatus(EventStatus.PUBLISHED),
                e -> e.setMaxParticipants(666L),
                e -> e.setStartingDateTime(LocalDateTime.now().plusDays(5)),
                e -> e.setEndingDateTime(LocalDateTime.now().plusDays(10)),
                e -> e.setRegistrationDeadline(LocalDateTime.now().plusDays(8))
        );
    }

    private User getSavedCreator() {
        User user = userFixture.getOne();
        return userRepository.save(user);
    }
}
