package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        Film newFilm = film.toBuilder().id(getNextId()).build();
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        Film oldFilm = films.get(film.getId());
        if (oldFilm == null) {
            throw new ConditionsNotMetException("такого фильма нет");
        }
        Film filmUpdated = oldFilm.toBuilder()
                .name((film.getName() != null) ? film.getName() : oldFilm.getName())
                .description((film.getDescription() != null) ? film.getDescription() : oldFilm.getDescription())
                .releaseDate((film.getReleaseDate() != null) ? film.getReleaseDate() : oldFilm.getReleaseDate())
                .duration((film.getDuration() != null) ? film.getDuration() : oldFilm.getDuration())
                .build();

        films.put(filmUpdated.getId(), filmUpdated);
        return filmUpdated;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
