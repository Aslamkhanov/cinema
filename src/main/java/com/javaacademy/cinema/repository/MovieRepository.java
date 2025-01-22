package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Optional.empty;

@Repository
@RequiredArgsConstructor
public class MovieRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Movie> findById(Integer id) {
        String sql = "select * from movie where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToProduct, id));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    private Movie mapToProduct(ResultSet rs, int rowNum) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setName(rs.getString("name"));
        movie.setDescription(rs.getString("description"));
        return movie;
    }
}
