package com.dyns.evento.users.dtos;

import com.dyns.evento.users.utils.UserValidationConstraints;
import com.dyns.evento.utils.GeneralConstraints;
import jakarta.validation.constraints.Email;
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
public class UserPatchRequest {
    @Email(
            regexp = UserValidationConstraints.EMAIL_PATTERN,
            message = UserValidationConstraints.INVALID_EMAIL_MESSAGE
    )
    private String email;

    @Size(max = UserValidationConstraints.ALIAS_MAX_LENGTH)
    @Pattern(
            regexp = GeneralConstraints.SAFE_TEXT_PATTERN,
            message = GeneralConstraints.INVALID_SAFE_TEXT_MESSAGE
    )
    private String alias;

    @Size(min = 1, max = UserValidationConstraints.FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @Size(min = 1, max = UserValidationConstraints.LAST_NAME_MAX_LENGTH)
    private String lastName;
}
