package com.javaacademy.cinema;

import com.javaacademy.cinema.config.SecurityProperty;
import com.javaacademy.cinema.dto.MovieDto;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "classpath:cleanup.sql")
public class MovieControllerTest {
    private final static String INVALID_TOKEN = "некорректный_токен";
    private final static String USER_TOKEN = "user-token";
    private final static String MOVIE_NAME = "kino";
    private final static String MOVIE_DESCRIPTION = "info";
    private final ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private final RequestSpecification requestSpecification =
            new RequestSpecBuilder()
                    .setBasePath("/movie")
                    .setContentType(ContentType.JSON)
                    .log(LogDetail.ALL)
                    .build();
    @Autowired
    private SecurityProperty securityProperty;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Успешное создания фильма")
    public void createMovieSuccess() {
        MovieDto movieDto = MovieDto.builder()
                .name(MOVIE_NAME)
                .description(MOVIE_DESCRIPTION)
                .build();
        MovieDto newMovieDto = given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .body(movieDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(MovieDto.class);
        assertEquals(movieDto.getName(), newMovieDto.getName());
        assertEquals(movieDto.getDescription(), newMovieDto.getDescription());
        assertNotNull(newMovieDto.getId());
    }

    @Test
    @DisplayName("Ошибка при создании фильма с некорректным токеном")
    public void createMovieWithInvalidToken() {
        MovieDto movieDto = MovieDto.builder()
                .name(MOVIE_NAME)
                .description(MOVIE_DESCRIPTION)
                .build();

        String response = given(requestSpecification)
                .header(USER_TOKEN, INVALID_TOKEN)
                .body(movieDto)
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
    @DisplayName("Получение всех фильмов")
    public void getAllMovieSuccess() {
        MovieDto movieDto = MovieDto.builder()
                .name(MOVIE_NAME)
                .description(MOVIE_DESCRIPTION)
                .build();
        given(requestSpecification)
                .header(USER_TOKEN, securityProperty.getToken())
                .body(movieDto)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());

        List<MovieDto> movieDtoList = given(requestSpecification)
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<MovieDto>>() {
                });
        assertFalse(movieDtoList.isEmpty());
        MovieDto getMovieDto = movieDtoList.get(0);
        assertEquals(movieDto.getName(), getMovieDto.getName());
        assertEquals(movieDto.getDescription(), getMovieDto.getDescription());
        assertNotNull(getMovieDto.getId());
    }
}
