package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<ErrorMessage> handleConditionsNotMetException(ConditionsNotMetException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorMessage("/films", exception.getMessage(), LocalDateTime.now(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String error = exception.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        log.error(error);
        return new ResponseEntity<>(new ErrorMessage("/films", error, LocalDateTime.now(), 400), HttpStatus.BAD_REQUEST);
    }

}
