package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class ErrorMessage {
    //private final String path;
    private final String error;
    private final LocalDateTime timestamp;
    //private final int status;

}
