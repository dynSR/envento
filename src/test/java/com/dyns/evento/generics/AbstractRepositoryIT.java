package com.dyns.evento.generics;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public abstract class AbstractRepositoryIT<
        T extends Identifiable<ID>, ID,
        TRepo extends JpaRepository<T, ID>,
        TFixture extends Fixture<T, ID>
        > {
    protected final TRepo underTest;

    @Autowired
    protected AbstractRepositoryIT(TRepo underTest) {
        this.underTest = underTest;
    }

    @Test
    @DisplayName("Should save and retrieve entity by ID")
    protected void shouldSaveAndRetrieveEntityById() {
        // Given & When
        T savedEntity = getSavedEntity();

        // Then
        assertNotNull(savedEntity.getId());
        assertTrue(underTest.findById(savedEntity.getId()).isPresent());
    }

    @Test
    @DisplayName("Should return all saved entities")
    protected void shouldReturnAllSavedEntities() {
        // Given & When
        saveEntities();
        Collection<? extends T> allSavedEntities = underTest.findAll();

        // Then
        assertNotNull(allSavedEntities);
        assertEquals(
                getFixture().getFixtureAmount(),
                allSavedEntities.size()
        );
    }

    @Test
    @DisplayName("Exception Test: Should not find deleted entity")
    protected void shouldReturnEmptyWhenFindingDeletedEntity() {
        // Given
        T savedEntity = getSavedEntity();

        // When
        underTest.deleteById(savedEntity.getId());

        // Then
        assertTrue(underTest.findById(savedEntity.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should only partially update an entity")
    protected abstract void shouldPartiallyUpdateEntity();

    @Test
    @DisplayName("Should overwrite entity fields when full updated")
    protected abstract void shouldFullyUpdateEntity();

    protected abstract TFixture getFixture();

    protected abstract T getSavedEntity();

    protected abstract void saveOneEntity();

    protected abstract void saveEntities();

    protected abstract Stream<Consumer<T>> getUpdateModifiers(boolean isPartialUpdate);
}
