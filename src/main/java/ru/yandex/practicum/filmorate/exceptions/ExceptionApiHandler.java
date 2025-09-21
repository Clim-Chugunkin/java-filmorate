package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(ValidationException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorMessage("/films", LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<ErrorMessage> handleConditionsNotMetException(ConditionsNotMetException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorMessage("/films", LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
