package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.cinema.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDto {
    private Integer id;
    @JsonProperty("movie_id")
    private Integer movieId;
    private LocalDateTime date;
    private BigDecimal price;
}
