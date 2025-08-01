package com.dyns.evento.users.dtos;

import com.dyns.evento.users.utils.UserValidationConstraints;
import com.dyns.evento.utils.GeneralConstraints;
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
public class UserPutRequest {
    @NotBlank
    @Email(
            regexp = UserValidationConstraints.EMAIL_PATTERN,
            message = UserValidationConstraints.INVALID_EMAIL_MESSAGE
    )
    private String email;

    @NotBlank
    @Size(max = UserValidationConstraints.ALIAS_MAX_LENGTH)
    @Pattern(
            regexp = GeneralConstraints.SAFE_TEXT_PATTERN,
            message = GeneralConstraints.INVALID_SAFE_TEXT_MESSAGE
    )
    private String alias;

    @NotBlank
    @Size(max = UserValidationConstraints.FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @NotBlank
    @Size(max = UserValidationConstraints.LAST_NAME_MAX_LENGTH)
    private String lastName;
}
