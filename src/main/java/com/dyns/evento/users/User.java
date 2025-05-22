package com.dyns.evento.users;

import com.dyns.evento.events.Event;
import com.dyns.evento.generics.Identifiable;
import com.dyns.evento.registrations.Registration;
import com.dyns.evento.users.utils.UserValidationConstraints;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails, Identifiable<UUID> {
    @PreRemove
    private void onPreRemove() {
//        clearEvents();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @NaturalId(mutable = true)
    @NotBlank
    @Email(
            regexp = UserValidationConstraints.EMAIL_PATTERN,
            message = UserValidationConstraints.INVALID_EMAIL_MESSAGE
    )
    @Column(nullable = false)
    private String email;

//    @NotBlank
//    @Pattern(
//            regexp = UserValidationConstraints.SAFE_TEXT_PATTERN,
//            message = UserValidationConstraints.INVALID_SAFE_TEXT_MESSAGE
//    )
//    @Column(nullable = false)
//    private String alias;

    @NotBlank
    @Size(max = UserValidationConstraints.FIRST_NAME_MAX_LENGTH)
    @Column(length = UserValidationConstraints.FIRST_NAME_MAX_LENGTH, nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = UserValidationConstraints.LAST_NAME_MAX_LENGTH)
    @Column(length = UserValidationConstraints.LAST_NAME_MAX_LENGTH, nullable = false)
    private String lastName;

    @NotBlank
    @Pattern(
            regexp = UserValidationConstraints.PASSWORD_PATTERN,
            message = UserValidationConstraints.INVALID_PASSWORD_PATTERN_MESSAGE
    )
    @Column(length = UserValidationConstraints.PASSWORD_MAX_LENGTH, nullable = false)
    private String password;

//    private boolean isEnabled = true;
//    private boolean isVerified = false;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private Set<Registration> registrations = new HashSet<>();

    @OneToMany(
            mappedBy = "creator",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private Set<Event> events = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(//));
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(email, user.email)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                email,
                firstName,
                lastName,
                password
        );
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public void addEvent(Event event) {
        if (events.contains(event)) return;
        events.add(event);
    }

    public void removeEvent(Event event) {
        if (!events.contains(event)) return;
        events.remove(event);
    }
}
