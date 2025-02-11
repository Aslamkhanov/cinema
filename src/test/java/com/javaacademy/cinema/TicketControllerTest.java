package com.javaacademy.cinema;

import com.javaacademy.cinema.config.SecurityProperty;
import com.javaacademy.cinema.dto.*;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.service.interfaces.MovieService;
import com.javaacademy.cinema.service.interfaces.SessionService;
import com.javaacademy.cinema.service.interfaces.TicketService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "classpath:cleanup.sql")
public class TicketControllerTest {
    private final static BigDecimal SESSION_PRICE = BigDecimal.valueOf(500);
    private final static LocalDateTime SESSION_TEST_LOCAL_DATE_TIME =
            LocalDateTime.of(2025, 11, 11, 11, 11);
    private final static String USER_TOKEN = "user-token";
    private final static String MOVIE_NAME = "kino";
    private final static String MOVIE_DESCRIPTION = "info";
    private final static String INVALID_TOKEN = "некорректный_токен";
    private final static Integer NON_EXISTENT_ID = 999999;
    private final ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private final RequestSpecification requestSpecification =
            new RequestSpecBuilder()
                    .setBasePath("/ticket")
                    .setContentType(ContentType.JSON)
                    .log(LogDetail.ALL)
                    .build();
    @Autowired
    private SecurityProperty securityProperty;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MovieService movieService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private SessionMapper sessionMapper;

    @Test
    @DisplayName("Успешное купить билет")
    public void bookTicketSuccess() {
        MovieDto movieDto = MovieDto.builder()
                .name(MOVIE_NAME)
                .description(MOVIE_DESCRIPTION)
                .build();
        MovieDto newMovieDto = movieService.save(movieDto);
        SessionDto sessionDto = SessionDto.builder()
                .movieId(newMovieDto.getId())
                .dateTime(SESSION_TEST_LOCAL_DATE_TIME)
                .price(SESSION_PRICE)
                .build();
        SessionDto newSessionDto = sessionService.save(sessionDto);

        TicketDto ticketDto = TicketDto.builder()
                .session(sessionMapper.toEntity(newSessionDto))
                .place(new Place(11, "A4"))
                .isBought(false)
                .build();
        ticketService.createTicketForSession(newSessionDto);
        String placeName = "A4";
        TicketRequestDto dto = new TicketRequestDto(newSessionDto.getId(),placeName);

        TicketResponseDto ticketResponseDto = given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .body(dto)
                .post("/booking")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(TicketResponseDto.class);
    }


//    @PostMapping("/booking")
//    public ResponseEntity<?> bookTicket(@RequestBody TicketRequestDto ticketRequestDto) {
//        try {
//            TicketResponseDto response = ticketService.bookTicket(ticketRequestDto.getSessionId(),
//                    ticketRequestDto.getPlaceName());
//            return ResponseEntity.ok(response);
//        } catch (TicketAlreadyBookedException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        }
//    }
    @Test
    @DisplayName("Получение всех купленных билетов")
    public void getAllBoughtTicketsSuccess() {


//        int statusCode = given(requestSpecification)
//                .header(USER_TOKEN, securityProperty.getToken())
//                .body(sessionDto)
//                .post()
//                .then()
//                .spec(responseSpecification)
//                .statusCode(HttpStatus.CREATED.value())
//                .extract()
//                .statusCode();
//        assertEquals(statusCode, HttpStatus.CREATED.value());
//
//        List<SessionDto> sessionDtoList = given(requestSpecification)
//                .header(USER_TOKEN, securityProperty.getToken())
//                .get("/saled")
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .extract()
//                .body()
//                .as(new TypeRef<List<SessionDto>>() {
//                });
//        assertFalse(sessionDtoList.isEmpty());
//        SessionDto getSessionDto = sessionDtoList.get(0);
//        assertEquals(sessionDto.getMovieId(), getSessionDto.getMovieId());
//        assertEquals(sessionDto.getDateTime(), getSessionDto.getDateTime());
//        assertEquals(0, sessionDto.getPrice().compareTo(getSessionDto.getPrice()));
//        assertNotNull(getSessionDto.getId());
    }

//    @GetMapping("/saled")
//    public ResponseEntity<?> getAllBoughtTickets(@RequestHeader(value = "user-token") String token) {
//        try {
//            securityService.checkIsAdmin(token);
//            return ResponseEntity.ok(ticketService.selectAll());
//        } catch (ForbiddenAccessException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//    }
}
