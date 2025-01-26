package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@RequiredArgsConstructor
@Repository
public class SessionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MovieRepository movieRepository;

    public Session createSession(Session session) {
        String sql = "insert into session (movie_id, date_time, price) values(?, ?, ?) returning id";
        Integer sessionId = jdbcTemplate.queryForObject(sql,
                Integer.class,
                session.getMovie().getId(),
                session.getDateTime(),
                session.getPrice());
        return findById(sessionId).get();
    }

    public List<Session> getAllSession() {
        String sql = "select * from session";
        return jdbcTemplate.query(sql, this::mapToSession);
    }

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
        session.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        session.setPrice(rs.getBigDecimal("price"));
        if (rs.getString("movie_id") != null) {
            Integer movieId = Integer.valueOf(rs.getString("movie_id"));
            session.setMovie(movieRepository.findById(movieId).orElse(null));
        }
        return session;
    }
}
