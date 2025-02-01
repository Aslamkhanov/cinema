package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.dto.TicketResponse;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
                ticket.getPlace().getId(),
                ticket.getSession().getId(),
                ticket.getIsBought());
        return findById(ticketId).get();
    }

    public void statusIsBought(Integer ticketId) throws TicketAlreadyBookedException {
        Ticket ticket = findById(ticketId).get();
        if (ticket.getIsBought()) {
            throw new TicketAlreadyBookedException("Билет уже куплен");
        }
        String sql = "update ticket set is_bought = true where id = ?";
        jdbcTemplate.update(sql, ticketId);
    }

    public List<Ticket> findTicketsBoughtTrue(Integer sessionId) {
        String sql = "select * from ticket where session_id = ? and is_bought = true";
        return jdbcTemplate.query(sql, this::mapToTicket, sessionId);
    }

    public List<Ticket> findTicketsBoughtFalse(Integer sessionId) {
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
    public void createTicketsForSession(Integer sessionId, List<Place> places) {
        String sql = "INSERT INTO ticket (place_id, session_id, is_bought) VALUES (?, ?, false)";
        for (Place place : places) {
            jdbcTemplate.update(sql, place.getId(), sessionId);
        }
    }

    public TicketResponse bookTicket(Integer sessionId, String placeName) throws TicketAlreadyBookedException {
        String sql = "SELECT id, is_bought FROM ticket WHERE session_id = ? AND place_name = ?";
        try {
            Ticket ticket = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Ticket t = new Ticket();
                t.setId(rs.getInt("id"));
                t.setIsBought(rs.getBoolean("is_bought"));
                return t;
            }, sessionId, placeName);
            statusIsBought(ticket.getId());
            String responseSql = """
                    SELECT t.id, p.name AS place_name, m.name AS movie_name, s.date
                    FROM ticket t
                    JOIN place p ON t.place_id = p.id
                    JOIN session s ON t.session_id = s.id
                    JOIN movie m ON s.movie_id = m.id
                    WHERE t.id = ?""";
            return jdbcTemplate.queryForObject(responseSql, (rs, rowNum) -> {
                TicketResponse response = new TicketResponse();
                response.setTicketId(rs.getInt("id"));
                response.setPlaceName(rs.getString("place_name"));
                response.setMovieName(rs.getString("movie_name"));
                response.setDate(rs.getTimestamp("date").toLocalDateTime());
                return response;
            }, ticket.getId());

        } catch (EmptyResultDataAccessException e) {
            throw new TicketAlreadyBookedException("Билет не найден");
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
