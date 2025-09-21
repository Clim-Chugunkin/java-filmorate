package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorMessage {
    private final String path;
    private final String error = "Internal Server Error";
    private final int status = 500;
    private final LocalDateTime timestamp;

}
