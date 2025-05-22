package com.dyns.evento.error.responses;

import com.dyns.evento.error.helpers.FieldError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidMethodArgumentErrorResponse {
    private HttpStatus status;
    private Set<FieldError> errors;
}
