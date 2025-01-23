package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.NewMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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

    public Optional<Movie> createMovie(NewMovie newMovie) {
        String sql = "insert into movie (name, description) values(?, ?) returning id";
        try {
            Integer generatedId = jdbcTemplate.queryForObject(sql, new Object[]{
                    newMovie.getName(),
                    newMovie.getDescription()
            }, Integer.class);

            return findById(generatedId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    //    public Optional<Movie> createMovieTest(NewMovie newMovie) {
//        String sql = "insert into movie (name, description) values(?, ?) returning id";
//        jdbcTemplate.update(sql, ps -> {
//            ps.setString(1, newMovie.getName());
//            ps.setString(2, newMovie.getDescription());
//        });
//        Optional<Movie> movie = findByName(newMovie.getName());
//        return findById(movie.get().getId());
//    }
//    private Optional<Movie> findByName(String name) {
//        String sql = "select * from movie where name = ?";
//        try {
//            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToMovie, name));
//        } catch (EmptyResultDataAccessException e) {
//            return empty();
//        }
//    }
    public List<Movie> selectAll() {
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
