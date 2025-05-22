package com.dyns.evento.users.dtos;

import com.dyns.evento.users.utils.UserValidationConstraints;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPostRequest {
    @NotBlank
    @Email(
            regexp = UserValidationConstraints.EMAIL_PATTERN,
            message = UserValidationConstraints.INVALID_EMAIL_MESSAGE
    )
    private String email;

    @NotBlank
    @Size(max = UserValidationConstraints.FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @NotBlank
    @Size(max = UserValidationConstraints.LAST_NAME_MAX_LENGTH)
    private String lastName;

    @NotBlank
    @Pattern(
            regexp = UserValidationConstraints.PASSWORD_PATTERN,
            message = UserValidationConstraints.INVALID_PASSWORD_PATTERN_MESSAGE
    )
    private String password;
}
