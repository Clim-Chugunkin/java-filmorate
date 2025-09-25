package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.adapter.LocalDateTimeAdapter;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ErrorMessage;
import ru.yandex.practicum.filmorate.model.Film;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(FilmController.class)
public class FilmorateFilmControllerTests {

    private static Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void init() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gb.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gson = gb.create();
    }

    //название не может быть пустым;
    @Test
    public void checkIfNameIsBlank() throws Exception {
        Film film = new Film(0L, "", "description", LocalDate.of(1999, 10, 10), 100);
        assertEquals("название не может быть пустым", postRequest(film));

    }

    //максимальная длина описания — 200 символов;
    @Test
    public void testMaxLengthDescription() throws Exception {
        Film film = new Film(0L, "name", "a".repeat(201), LocalDate.of(1999, 10, 10), 100);
        assertEquals("максимальная длина описания не более 200 символов", postRequest(film));
    }

    //дата релиза — не раньше 28 декабря 1895 года;
    @Test
    public void releaseDayCheck() throws Exception {
        Film film = new Film(0L, "name", "a".repeat(200), LocalDate.of(1895, 12, 27), 100);
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", postRequest(film));
    }

    //продолжительность фильма должна быть положительным числом
    @Test
    public void durationCheck() throws Exception {
        Film film = new Film(0L, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), -10);
        assertEquals("продолжительность должна быть положительной", postRequest(film));
    }

    //попытка обновить фильм без id
    @Test
    public void updateWithoutID() throws Exception {
        Film film = new Film(null, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), 10);
        assertEquals("Id должен быть указан", putRequest(film));
    }

    //попытка обновить фильм id которого нет
    @Test
    public void updateWithUnexistedID() throws Exception {
        Film film = new Film(1000L, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), 0);
        assertEquals("такого фильма нет", putRequest(film));
    }

    private String postRequest(Film film) throws Exception {
        String filmJson = gson.toJson(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(filmJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ErrorMessage msg = gson.fromJson(json, ErrorMessage.class);
        return msg.getError();
    }

    private String putRequest(Film film) throws Exception {
        String filmJson = gson.toJson(film);

        MvcResult result = mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(filmJson))
                .andExpect(status().isNotFound())
                .andReturn();
        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ErrorMessage msg = gson.fromJson(json, ErrorMessage.class);
        return msg.getError();
    }
}