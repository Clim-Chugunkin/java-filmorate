package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConditionsNotMetException.class)
    public ErrorMessage handleConditionsNotMetException(ConditionsNotMetException exception) {
        log.error(exception.getMessage());
        return new ErrorMessage(exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String error = exception.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        log.error(error);
        return new ErrorMessage(error, LocalDateTime.now());
    }
}
