package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.TicketResponse;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
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

    public Ticket save(Ticket ticket) {
        String sql = "insert into ticket (place_id, session_id, is_bought) values(?, ?, ?) returning id";
        Integer ticketId = jdbcTemplate.queryForObject(sql,
                Integer.class,
                ticket.getPlace().getId(),
                ticket.getSession().getId(),
                ticket.getIsBought());
        ticket.setId(ticketId);
        return ticket;
    }

    private void buyTicket(Integer ticketId) {
        Ticket ticket = findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Билет с id " + ticketId + " не найден"));
        if (ticket.getIsBought()) {
            throw new TicketAlreadyBookedException();
        }
        String sql = "update ticket set is_bought = true where id = ?";
        jdbcTemplate.update(sql, ticketId);
    }

    public List<String> selectFreePlace(Integer sessionId) {
        String sql = """
                    select p.number
                    from place p
                    where p.id not in (
                        select t.place_id from ticket t where t.session_id = ? and t.is_bought = true
                    )
                    order by p.id;
                """;
        return jdbcTemplate.queryForList(sql, String.class, sessionId);
    }

    public List<Ticket> getSoldTickets(Integer sessionId) {
        String sql = "select * from ticket where session_id = ? and is_bought = true";
        return jdbcTemplate.query(sql, this::mapToTicket, sessionId);
    }

    public List<Ticket> getUnsoldTickets(Integer sessionId) {
        String sql = "select * from ticket where session_id = ? and is_bought = false";
        return jdbcTemplate.query(sql, this::mapToTicket, sessionId);
    }

    public List<Ticket> selectAll() {
        String sql = """
                   select *
                   from ticket
                   where is_bought = true
                """;
        return jdbcTemplate.query(sql, this::mapToTicket);
    }

    public Optional<Ticket> findById(Integer id) {
        String sql = "select * from ticket where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToTicket, id));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    public TicketResponse bookTicket(Integer sessionId, String placeName) {
        String sql = """
                select t.id, t.is_bought, t.session_id, t.place_id
                from ticket t
                join place p on t.place_id = p.id
                where t.session_id = ? and p.number = ?
                """;
        Ticket ticket = jdbcTemplate.queryForObject(sql,
                this::mapToTicket,
                sessionId,
                placeName);
        buyTicket(ticket.getId());
        String responseSql = """
                SELECT t.id, p.number AS place_name, m.name AS movie_name, s.date_time, s.id AS session_id
                FROM ticket t
                JOIN place p ON t.place_id = p.id
                JOIN session s ON t.session_id = s.id
                JOIN movie m ON s.movie_id = m.id
                WHERE t.id = ?
                """;
        return jdbcTemplate.queryForObject(responseSql, this::mapToResponse, ticket.getId());
    }

    private TicketResponse mapToResponse(ResultSet rs, int rowNum) throws SQLException {
        TicketResponse response = new TicketResponse();
        response.setTicketId(rs.getInt("id"));
        response.setPlaceName(rs.getString("place_name"));
        response.setMovieName(rs.getString("movie_name"));
        response.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        return response;
    }

    private Ticket mapToTicket(ResultSet rs, int rowNum) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        if (rs.getString("session_id") != null) {
            Integer sessionId = Integer.valueOf(rs.getString("session_id"));
            sessionRepository.findById(sessionId).ifPresent(ticket::setSession);
        }
        if (rs.getString("place_id") != null) {
            Integer placeId = Integer.valueOf(rs.getString("place_id"));
            placeRepository.findById(placeId).ifPresent(ticket::setPlace);
        }
        ticket.setIsBought(rs.getBoolean("is_bought"));
        return ticket;
    }
}
