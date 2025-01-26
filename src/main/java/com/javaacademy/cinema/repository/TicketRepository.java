package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.IsBoughtException;
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
public class TicketRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SessionRepository sessionRepository;
    private final PlaceRepository placeRepository;

    public Ticket createTicket(Ticket ticket) {
        String sql = "insert into ticket (place_id, session_id, is_bought) values(?, ?, ?) returning id";
        Integer ticketId = jdbcTemplate.queryForObject(sql,
                Integer.class,
                ticket.getPlace().getNumber(),
                ticket.getSession().getId(),
                ticket.getIsBought());
        return findById(ticketId).get();
    }

    public void statusIsBought(Integer ticketId) throws IsBoughtException {
        Ticket ticket = findById(ticketId).get();
        if (ticket.getIsBought()) {
            throw new IsBoughtException("Билет уже куплен");
        }
        String sql = "update ticket set is_bought = true where id = ?";
        jdbcTemplate.update(sql, ticketId);
    }

    public List<Ticket> findBoughtTickets(Integer sessionId) {
        String sql = "select * from ticket where session_id = ? and is_bought = true";
        return jdbcTemplate.query(sql, this::mapToTicket, sessionId);
    }

    public List<Ticket> findTicketsNotBought(Integer sessionId) {
        String sql = "select * from ticket where session_id = ? and is_bought = false";
        return jdbcTemplate.query(sql, this::mapToTicket, sessionId);
    }

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
        if (rs.getString("session_id") != null) {
            Integer sessionId = Integer.valueOf(rs.getString("session_id"));
            ticket.setSession(sessionRepository.findById(sessionId).orElse(null));
        }
        if (rs.getString("place_id") != null) {
            Integer placeId = Integer.valueOf(rs.getString("place_id"));
            ticket.setPlace(placeRepository.findById(placeId).orElse(null));
        }
        ticket.setIsBought(rs.getBoolean("is_bought"));
        return ticket;
    }
}
