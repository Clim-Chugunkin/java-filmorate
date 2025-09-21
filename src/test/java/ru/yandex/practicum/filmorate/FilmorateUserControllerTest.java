package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmorateUserControllerTest {

    private final UserController uc = new UserController();

    //электронная почта не может быть пустой и должна содержать символ @;
    @Test
    public void emailTest() {
        User user = new User(
                0L,
                "mail.ru",
                "login",
                "name",
                LocalDate.of(1985, 8, 10)
        );
        Exception exception = assertThrows(ValidationException.class, () -> {
            uc.addUser(user);
        });
        String expectedMessage = "электронная почта не может быть " +
                "пустой и должна содержать символ @";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

    }

    //логин не может быть пустым и содержать пробелы
    @Test
    public void loginTest() {
        User user = new User(
                0L,
                "mail@.ru",
                "log in",
                "name",
                LocalDate.of(1985, 8, 10)
        );
        Exception exception = assertThrows(ValidationException.class, () -> {
            uc.addUser(user);
        });
        String expectedMessage = "логин не может быть пустым и содержать пробелы";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    //дата рождения не может быть в будущем
    @Test
    public void birthdayTest() {
        User user = new User(
                0L,
                "mail@.ru",
                "login",
                "name",
                LocalDate.of(2025, 12, 10)
        );
        Exception exception = assertThrows(ValidationException.class, () -> {
            uc.addUser(user);
        });
        String expectedMessage = "дата рождения не может быть в будущем";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateUser() {
        User user = new User(
                0L,
                "mail@.ru",
                "login",
                "name",
                LocalDate.of(2024, 12, 10)
        );
        assertDoesNotThrow(() -> uc.addUser(user));
        User tempUser = (User) uc.getUsers().toArray()[0];
        Long userID = tempUser.getId();
        User userUpdated = new User(
                userID,
                "mail@.ru",
                "loginUpdated",
                "nameUpdated",
                LocalDate.of(2024, 12, 10)
        );
        assertDoesNotThrow(() -> uc.update(userUpdated));
        User newUser = uc.getUsers()
                .stream()
                .filter((it) -> Objects.equals(it.getId(), userID))
                .findFirst()
                .orElse(null);
        assertEquals("mail@.ru", newUser.getEmail());
        assertEquals("loginUpdated", newUser.getLogin());
        assertEquals("nameUpdated", newUser.getName());
    }

    //попытка обновить пользователя без id
    @Test
    public void updateWithoutID() {
        User user = new User(
                null,
                "mail@.ru",
                "login",
                "name",
                LocalDate.of(2024, 12, 10)
        );
        Exception exception = assertThrows(ConditionsNotMetException.class, () -> {
            uc.update(user);
        });
        String expectedMessage = "Id должен быть указан";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    //попытка обновить пользователя id которого нет
    @Test
    public void updateWithUnexistedID() {
        User user = new User(
                1000L,
                "mail@.ru",
                "login",
                "name",
                LocalDate.of(2024, 12, 10)
        );
        Exception exception = assertThrows(ConditionsNotMetException.class, () -> {
            uc.update(user);
        });
        String expectedMessage = "такого пользовтеля нет";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

}
