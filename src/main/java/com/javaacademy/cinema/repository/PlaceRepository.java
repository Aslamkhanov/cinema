package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Place;
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
public class PlaceRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Place> findAllPlaces() {
        String sql = "select number from place";
        return jdbcTemplate.query(sql, this::mapToPlace);
    }

    public Optional<Place> findById(Integer id) {
        String sql = "select * from place where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToPlace, id));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    private Place mapToPlace(ResultSet rs, int rowNum) throws SQLException {
        Place place = new Place();
        place.setId(rs.getInt("id"));
        place.setNumber(rs.getString("number"));
        return place;
    }
}
