package com.javaacademy.cinema;

import com.javaacademy.cinema.config.security.SecurityProperty;
import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.service.interfaces.MovieService;
import com.javaacademy.cinema.service.interfaces.SessionService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "classpath:cleanup.sql")
public class SessionControllerTest {
    private final static BigDecimal SESSION_PRICE = BigDecimal.valueOf(500);
    private final static LocalDateTime SESSION_TEST_LOCAL_DATE_TIME =
            LocalDateTime.of(2025, 11, 11, 11, 11);
    private final static String USER_TOKEN = "user-token";
    private final static String MOVIE_NAME = "kino";
    private final static String MOVIE_DESCRIPTION = "info";
    private final static String INVALID_TOKEN = "некорректный_токен";
    private final static Integer NON_EXISTENT_ID = 999999;
    @Value("${server.port}")
    int port;
    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;
    @Autowired
    private SecurityProperty securityProperty;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MovieService movieService;
    @Autowired
    private SessionService sessionService;

    @BeforeEach
    void setup() {
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();

        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .setBasePath("/session")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    @DisplayName("Успешное создания сессии")
    public void createSessionsSuccess() {
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

        int statusCode = given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .body(sessionDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .statusCode();
        assertEquals(statusCode, HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Ошибка при создании сессии с некорректным токеном")
    public void createSessionWithInvalidToken() {
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

        String response = given(requestSpecification)
                .header(USER_TOKEN, INVALID_TOKEN)
                .body(sessionDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract()
                .asString();
        String expected = "Нет прав доступа, авторизуйтесь как администратор";
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Ошибка при создании сессии с несуществующим фильмом")
    public void createSessionWithNonExistentMovie() {
        SessionDto sessionDto = SessionDto.builder()
                .movieId(NON_EXISTENT_ID)
                .dateTime(SESSION_TEST_LOCAL_DATE_TIME)
                .price(SESSION_PRICE)
                .build();

        String response = given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .body(sessionDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .asString();

        String expected = "Билет с id 999999 не найден";
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Получение всех сессий")
    public void getAllSessionSuccess() {
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

        int statusCode = given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .body(sessionDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .statusCode();
        assertEquals(statusCode, HttpStatus.CREATED.value());

        List<SessionDto> sessionDtoList = given(requestSpecification)
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<SessionDto>>() {
                });
        assertFalse(sessionDtoList.isEmpty());
        SessionDto getSessionDto = sessionDtoList.get(0);
        assertEquals(sessionDto.getMovieId(), getSessionDto.getMovieId());
        assertEquals(sessionDto.getDateTime(), getSessionDto.getDateTime());
        assertEquals(0, sessionDto.getPrice().compareTo(getSessionDto.getPrice()));
        assertNotNull(getSessionDto.getId());
    }

    @Test
    @DisplayName("Получение всех сессий по sessionId")
    public void getFreePlacesSuccess() {
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

        given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .body(sessionDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value());
        String expected = "[\"A1\",\"A2\",\"A3\",\"A4\",\"A5\",\"B1\",\"B2\",\"B3\",\"B4\",\"B5\"]";

        String response = given(requestSpecification)
                .param("sessionId", newSessionDto.getId())
                .get("/" + newSessionDto.getId() + "/free-place")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .asString();
        assertEquals(expected, response);
    }
}
