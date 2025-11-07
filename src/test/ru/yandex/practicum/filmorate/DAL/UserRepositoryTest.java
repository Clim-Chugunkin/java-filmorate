package ru.yandex.practicum.filmorate.DAL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
public class UserRepositoryTest {
    @Autowired
    @Qualifier("userRepository")
    private UserStorage userStorage;

    @Test
    public void addAndGetUser() {
        //добавляем пользователя
        User user = userStorage.addUser(User.builder()
                .email("sergey@mail.ru")
                .login("sergeylogin")
                .name("sergey")
                .birthday(LocalDate.of(1985, 8, 10))
                .build());

        assertEquals("sergey", userStorage.getUserById(user.getId()).getName());
    }

    @Test
    public void addAndGetListOfUsers(){
        User user1 = userStorage.addUser(User.builder()
                .email("sergey@mail.ru")
                .login("sergeylogin")
                .name("sergey")
                .birthday(LocalDate.of(1985, 8, 10))
                .build());
        User user2 = userStorage.addUser(User.builder()
                .email("anton@mail.ru")
                .login("antonlogin")
                .name("anton")
                .birthday(LocalDate.of(1985, 8, 10))
                .build());

        assertEquals(2,userStorage.getUsers().size());
    }

    @Test
    public void updateUserTest(){
        User user = userStorage.addUser(User.builder()
                .email("sergey@mail.ru")
                .login("sergeylogin")
                .name("sergey")
                .birthday(LocalDate.of(1985, 8, 10))
                .build());
        User updated = user.toBuilder()
                .name("name updated")
                .build();
        userStorage.update(updated);
        assertEquals("name updated", userStorage.getUserById(user.getId()).getName());
    }

}