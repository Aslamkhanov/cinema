package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Place;
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
public class PlaceRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Place> findById(Integer id) {
        String sql = "select * from place where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToProduct, id));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    private Place mapToProduct(ResultSet rs, int rowNum) throws SQLException {
        Place place = new Place();
        place.setId(rs.getInt("id"));
        place.setNumber(rs.getString("number"));
        return place;
    }
}
