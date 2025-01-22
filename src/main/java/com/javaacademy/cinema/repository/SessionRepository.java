package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.empty;

@RequiredArgsConstructor
@Repository
public class SessionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MovieRepository movieRepository;

    public Optional<Session> findById(Integer id) {
        String sql = "select * from session where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToSession, id));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    private Session mapToSession(ResultSet rs, int rowNum) throws SQLException {
        Session session = new Session();
        session.setId(rs.getInt("id"));
        Timestamp timestamp = rs.getTimestamp("date_time");
        if (timestamp != null) {
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            session.setDateTime(localDateTime);
        }
        session.setPrice(rs.getBigDecimal("price"));
        if (rs.getString("movie_id") != null) {
            Integer movieId = Integer.valueOf(rs.getString("movie_id"));
            session.setMovieId(movieRepository.findById(movieId).orElse(null));
        }
        return session;
    }
}
