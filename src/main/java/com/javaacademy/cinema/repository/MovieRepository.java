package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@Repository
@RequiredArgsConstructor
public class MovieRepository {
    private final JdbcTemplate jdbcTemplate;

    public Movie createMovie(Movie movie) {
        String sql = "insert into movie (name, description) values(?, ?) returning id";
        Integer generatedId = jdbcTemplate.queryForObject(sql,
                Integer.class,
                movie.getName(),
                movie.getDescription());
        movie.setId(generatedId);
        return movie;
    }

    public List<Movie> getAllMovie() {
        String sql = "select * from movie";
        return jdbcTemplate.query(sql, this::mapToMovie);
    }

    public Optional<Movie> findById(Integer id) {
        String sql = "select * from movie where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToMovie, id));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    private Movie mapToMovie(ResultSet rs, int rowNum) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setName(rs.getString("name"));
        movie.setDescription(rs.getString("description"));
        return movie;
    }
}
