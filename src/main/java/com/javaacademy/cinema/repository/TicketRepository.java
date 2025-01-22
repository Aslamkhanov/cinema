package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Optional.empty;

@RequiredArgsConstructor
@Repository
public class TicketRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SessionRepository sessionRepository;
    private final PlaceRepository placeRepository;

    public Optional<Ticket> findById(Integer id) {
        String sql = "select * from ticket where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToTicket, id));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    private Ticket mapToTicket(ResultSet rs, int rowNum) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        if (rs.getString("place_id") != null) {
            Integer producerId = Integer.valueOf(rs.getString("place_id"));
            ticket.setPlaceId(placeRepository.findById(producerId).orElse(null));
        }
        if (rs.getString("session_id") != null) {
            Integer producerId = Integer.valueOf(rs.getString("session_id"));
            ticket.setSessionId(sessionRepository.findById(producerId).orElse(null));
        }
        ticket.setBoughtOrNot(rs.getString("is_bought"));
        return ticket;
    }
}
