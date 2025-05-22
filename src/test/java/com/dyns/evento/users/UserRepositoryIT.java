package com.dyns.evento.users;

import com.dyns.evento.events.Event;
import com.dyns.evento.events.EventFixture;
import com.dyns.evento.events.services.EventRepository;
import com.dyns.evento.generics.AbstractRepositoryIT;
import com.dyns.evento.users.services.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class UserRepositoryIT extends AbstractRepositoryIT<User, UUID, UserRepository, UserFixture> {
    private final EventRepository eventRepository;
    private final EventFixture eventFixture = new EventFixture();

    @Autowired
    private UserRepositoryIT(
            UserRepository underTest,
            EventRepository eventRepository
    ) {
        super(underTest);
        this.eventRepository = eventRepository;
    }

    @Test
    @DisplayName("Should only partially update user")
    @Override
    protected void shouldPartiallyUpdateEntity() {
        // Given
        User savedEvent = getSavedEntity();

        // When & Then
        underTest.findById(savedEvent.getId()).ifPresent(
                u -> {
                    getUpdateModifiers(true).forEach(
                            modifier -> modifier.accept(u)
                    );

                    User updatedUser = underTest.save(u);
                    assertEquals(
                            u.getEmail(),
                            updatedUser.getEmail()
                    );
                    assertEquals(
                            u.getFirstName(),
                            updatedUser.getFirstName()
                    );
                }
        );

        // Given
        User savedUser = getSavedEntity();

        // When
        savedUser.setFirstName("PartialUpdatedName");
        User updatedUser = underTest.save(savedUser);

        // Then
        assertEquals("PartialUpdatedName", updatedUser.getFirstName());
        assertEquals(savedUser.getEmail(), updatedUser.getEmail());
        assertEquals(savedUser.getLastName(), updatedUser.getLastName());
        assertEquals(savedUser.getPassword(), updatedUser.getPassword());
    }

    @Test
    @DisplayName("Should overwrite user fields when full updated")
    @Override
    protected void shouldFullyUpdateEntity() {
        // Given & When
        User savedUser = getSavedEntity();
        // When & Then
        underTest.findById(savedUser.getId()).ifPresent(
                u -> {
                    getUpdateModifiers(false)
                            .forEach(modifier -> modifier.accept(u));

                    User updatedUser = underTest.save(u);
                    assertEquals(
                            u.getEmail(),
                            updatedUser.getEmail()
                    );
                    assertEquals(
                            u.getFirstName(),
                            updatedUser.getFirstName()
                    );
                    assertEquals(
                            u.getLastName(),
                            updatedUser.getLastName()
                    );
                    assertEquals(
                            u.getPassword(),
                            updatedUser.getPassword()
                    );
                }
        );
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // Given
        User savedUser = getSavedEntity();

        // When
        Optional<User> foundUser = underTest.findByEmail(savedUser.getEmail());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should remove all events of deleted user")
    void shouldRemoveEventsOfDeletedUser() {
        // Given
        User john = getSavedEntity();

        Event musicFest = eventFixture.getOne();
        musicFest.setTitle("Music Fest");
        musicFest.setCreator(john);
        eventRepository.save(musicFest);

        Event computerContest = eventFixture.getOne();
        computerContest.setTitle("Computer Contest");
        computerContest.setCreator(john);
        eventRepository.save(computerContest);

        // When
        underTest.deleteById(john.getId());
        Collection<Event> allEvents = eventRepository.findAll();
        log.info(eventRepository.findAll().toString());

        // Then
        assertNotNull(allEvents);
        assertEquals(0, allEvents.size());
    }

    @Override
    protected UserFixture getFixture() {
        return new UserFixture();
    }

    @Override
    protected User getSavedEntity() {
        User user = getFixture().getOne();
        return underTest.save(user);
    }

    @Override
    protected void saveOneEntity() {
        User user = getFixture().getOne();
        underTest.save(user);
    }

    @Override
    protected void saveEntities() {
        getFixture()
                .getMany()
                .forEach(underTest::save);
    }

    /**
     * Update full object or only email and firstname
     * @param isPartialUpdate
     * @return a set of modifier to apply to a user
     */
    @Override
    protected Stream<Consumer<User>> getUpdateModifiers(boolean isPartialUpdate) {
        if (isPartialUpdate)
            return Stream.of(
                    u -> u.setEmail("new.updated@domain.fr"),
                    u -> u.setFirstName("UpdatedFirstName")
            );

        return Stream.of(
                u -> u.setEmail("new.updated@domain.fr"),
                u -> u.setFirstName("UpdatedFirstName"),
                u -> u.setLastName("UpdatedLastName"),
                u -> u.setPassword("69.;Lr9fY(TqB1r0}DL")
        );
    }
}
