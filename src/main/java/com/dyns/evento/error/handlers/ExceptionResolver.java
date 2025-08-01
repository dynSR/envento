package com.dyns.evento.error.handlers;

import com.dyns.evento.error.exceptions.InvalidCredentialsException;
import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.error.helpers.FieldError;
import com.dyns.evento.error.responses.ErrorResponse;
import com.dyns.evento.error.responses.InvalidMethodArgumentErrorResponse;
import com.dyns.evento.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(
            NotFoundException exception
    ) {
        log.info("Caught NotFoundException: {}", exception.getMessage());

        var error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handle(
            InvalidCredentialsException exception
    ) {
        log.info("Caught InvalidCredentialsException: {}", exception.getMessage());

        var error = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("Invalid username or password.")
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handle(
            DataIntegrityViolationException exception
    ) {
        log.info("Caught DataIntegrityViolationException: {}", exception.getMessage());

        var error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .message("A data conflict occurred. Please try again.")
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handle(
            IllegalArgumentException exception
    ) {
        log.info("Caught IllegalArgumentException: {}", exception.getMessage());

        var error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Unable to process the request, an argument is either missing or invalid.")
                .build();
        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvalidMethodArgumentErrorResponse> handle(
            MethodArgumentNotValidException exception
    ) {
        Set<FieldError> errors = new HashSet<>();
        exception.getFieldErrors()
                .forEach(fieldError ->
                        errors.add(FieldError.builder()
                                .field(fieldError.getField())
                                .message(StringUtils.capitalized(fieldError.getDefaultMessage()))
                                .build()
                        )
                );


        log.info("Caught MethodArgumentNotValidException: {}",
                exception.getObjectName() + errors
        );


        var error = InvalidMethodArgumentErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errors(errors)
                .build();
        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handle(
            HttpMessageNotReadableException exception
    ) {
        log.info("Caught HttpMessageNotReadableException: {}", exception.getMessage());

        var fullMessage = exception.getMostSpecificCause().getMessage();
        Pattern pattern = Pattern.compile("\"(.*?)\": not one of the values accepted for Enum class: \\[(.*?)]");
        Matcher matcher = pattern.matcher(fullMessage);

        var cleanMessageBuilder = new StringBuilder();
        if (matcher.find()) {
            cleanMessageBuilder
                    .append("'")
                    .append(matcher.group(1))
                    .append("' ")
                    .append("not one of the accepted values ")
                    .append("[")
                    .append(matcher.group(2))
                    .append("]");
        } else {
            cleanMessageBuilder.append(exception.getMostSpecificCause().getMessage());
        }

        var error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Invalid request format: " + cleanMessageBuilder)
                .build();
        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(
            Exception exception
) {
        log.info(
                "Caught Exception: ",
                exception
        );

        var error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred, please try again later.")
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
