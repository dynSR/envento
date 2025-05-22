package com.dyns.evento.users;

import com.dyns.evento.generics.Fixture;

import java.util.*;

public class UserFixture implements Fixture<User, UUID> {
    private final List<User> users = new ArrayList<>();
    private final List<String> firstNames = Arrays.asList("John", "Jane", "Alice");
    private final List<String> lastNames = Arrays.asList("Doe", "Due", "Smith");
    public static final String USER_DEFAULT_VALID_PASSWORD = "63~;~r9fY(TqB1r0}BP";

    @Override
    public User getOne() {
        return User.builder()
                .id(null)
                .email("john.doe@domain.com")
                .firstName("John")
                .lastName("Doe")
                .password(USER_DEFAULT_VALID_PASSWORD)
                .events(new HashSet<>())
                .registrations(new HashSet<>())
                .build();
    }

    @Override
    public Collection<? extends User> getMany() {
        for (int i = 0; i < getFixtureAmount(); i++) {
            String email = String.format("valid_%d@email.com", i);
            users.add(User.builder()
                    .id(null)
                    .email(email)
                    .firstName(firstNames.get(i))
                    .lastName(lastNames.get(i))
                    .password(USER_DEFAULT_VALID_PASSWORD)
                    .events(new HashSet<>())
                    .registrations(new HashSet<>())
                    .build()
            );
        }
        return users;
    }

    @Override
    public int getFixtureAmount() {
        return 3;
    }
}
