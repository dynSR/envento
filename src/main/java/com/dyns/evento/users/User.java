package com.dyns.evento.users;

import com.dyns.evento.events.Event;
import com.dyns.evento.generic.Identifiable;
import com.dyns.evento.registrations.Registration;
import com.dyns.evento.users.utils.DefaultUser;
import com.dyns.evento.users.utils.UserConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails, Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id = UUID.randomUUID();

    @NaturalId
    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email = DefaultUser.EMAIL;

    @NotBlank
    @Size(max = UserConstants.FIRST_NAME_LENGTH)
    @Column(length = UserConstants.FIRST_NAME_LENGTH, nullable = false)
    private String firstName = DefaultUser.FIRST_NAME;

    @NotBlank
    @Size(max = UserConstants.LAST_NAME_LENGTH)
    @Column(length = UserConstants.LAST_NAME_LENGTH, nullable = false)
    private String lastName = DefaultUser.LAST_NAME;


    @NotBlank
    @Size(
            min = UserConstants.PASSWORD_MIN_LENGTH,
            max = UserConstants.PASSWORD_MAX_LENGTH
    )
    @Column(length = UserConstants.PASSWORD_MAX_LENGTH, nullable = false)
    private String password = DefaultUser.PASSWORD;

    @OneToMany(targetEntity = Registration.class)
    private List<Registration> registrations = new ArrayList<>();

    @OneToMany(targetEntity = Registration.class)
    private List<Event> events = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
}
