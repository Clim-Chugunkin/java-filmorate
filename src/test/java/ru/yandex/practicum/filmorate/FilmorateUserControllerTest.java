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
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ErrorMessage;
import ru.yandex.practicum.filmorate.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class FilmorateUserControllerTest {
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

    //электронная почта не может быть пустой и должна содержать символ @;
    @Test
    public void emailTest() throws Exception {
        User user = new User(
                0L,
                "mail.ru",
                "login",
                "name",
                LocalDate.of(1985, 8, 10)
        );
        assertEquals("почта указана неправильно", postRequest(user));
    }

    //логин не может быть пустым и содержать пробелы
    @Test
    public void loginTest() throws Exception {
        User user = new User(
                0L,
                "mail@mail.ru",
                "log in",
                "name",
                LocalDate.of(1985, 8, 10)
        );
        assertEquals("логин не должен содержать пробелы", postRequest(user));
    }

    //дата рождения не может быть в будущем
    @Test
    public void birthdayTest() throws Exception {
        User user = new User(
                0L,
                "mail@mail.ru",
                "login",
                "name",
                LocalDate.of(2025, 12, 10)
        );
        assertEquals("дата рождения не может быть в будущем", postRequest(user));
    }



    //попытка обновить пользователя без id
    @Test
    public void updateWithoutID() throws Exception {
        User user = new User(
                null,
                "mail@.ru",
                "login",
                "name",
                LocalDate.of(2024, 12, 10)
        );
        assertEquals("Id должен быть указан", putRequest(user));
    }

    //попытка обновить пользователя id которого нет
    @Test
    public void updateWithUnexistedID() throws Exception {
        User user = new User(
                1000L,
                "mail@.ru",
                "login",
                "name",
                LocalDate.of(2024, 12, 10)
        );
        assertEquals("такого пользовтеля нет", putRequest(user));
    }

    private String postRequest(User user) throws Exception {
        String filmJson = gson.toJson(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(filmJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ErrorMessage msg = gson.fromJson(json, ErrorMessage.class);
        return msg.getError();
    }
    private String putRequest(User user) throws Exception {
        String filmJson = gson.toJson(user);

        MvcResult result = mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(filmJson))
                .andExpect(status().isNotFound())
                .andReturn();
        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ErrorMessage msg = gson.fromJson(json, ErrorMessage.class);
        return msg.getError();
    }



}
