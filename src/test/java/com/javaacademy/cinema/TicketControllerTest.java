package com.javaacademy.cinema;

import com.javaacademy.cinema.config.SecurityProperty;
import com.javaacademy.cinema.dto.*;
import com.javaacademy.cinema.entity.Place;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "classpath:cleanup.sql")
public class TicketControllerTest {
    private final static String PLACE_NAME = "A1";
    private final static BigDecimal SESSION_PRICE = BigDecimal.valueOf(500);
    private final static LocalDateTime SESSION_TEST_LOCAL_DATE_TIME =
            LocalDateTime.of(2025, 11, 11, 11, 11);
    private final static String USER_TOKEN = "user-token";
    private final static String MOVIE_NAME = "kino";
    private final static String MOVIE_DESCRIPTION = "info";
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
                .place(new Place(11, "A1"))
                .isBought(false)
                .build();
        ticketService.save(ticketDto);
        TicketRequestDto dto = new TicketRequestDto(newSessionDto.getId(), PLACE_NAME);

        TicketResponseDto ticketResponseDto = given(requestSpecification)
                .body(dto)
                .post("/booking")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(TicketResponseDto.class);
        assertEquals(MOVIE_NAME, ticketResponseDto.getMovieName());
        assertEquals(PLACE_NAME, ticketResponseDto.getPlaceName());
        assertEquals(SESSION_TEST_LOCAL_DATE_TIME, ticketResponseDto.getDateTime());
    }
    @Test
    @DisplayName("Ошибка при попытке купить уже купленный билет")
    public  void bookTicketConflict() {
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
                .place(new Place(11, "A1"))
                .isBought(true)
                .build();
        ticketService.save(ticketDto);
        TicketRequestDto dto = new TicketRequestDto(newSessionDto.getId(), PLACE_NAME);

        int statusCode = given(requestSpecification)
                .body(dto)
                .post("/booking")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .statusCode();
        assertEquals(statusCode, HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("Получение всех купленных билетов")
    public void getAllBoughtTicketsSuccess() {
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
                .place(new Place(11, "A1"))
                .isBought(false)
                .build();
        ticketService.save(ticketDto);
        TicketRequestDto dto = new TicketRequestDto(newSessionDto.getId(), PLACE_NAME);

       given(requestSpecification)
                .body(dto)
                .post("/booking")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<TicketDto> ticketDtoList = given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .get("/saled")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<TicketDto>>() {
                });
        assertFalse(ticketDtoList.isEmpty());
        TicketDto getTicketDto = ticketDtoList.get(0);
        assertEquals(true, getTicketDto.getIsBought());
    }
}
