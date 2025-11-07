package ru.yandex.practicum.filmorate.DAL.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<FilmDTO> {
    @Override
    public FilmDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmDTO.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .mpa(Mpa.builder()
                        .id(rs.getLong("rating_id"))
                        .name(rs.getString("mpa"))
                        .build())
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(LocalDate.parse(rs.getString("release")))
                .build();
    }
}
