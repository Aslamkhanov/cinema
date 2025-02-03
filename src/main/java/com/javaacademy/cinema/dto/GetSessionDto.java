package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GetSessionDto {
    private Integer id;
    @JsonProperty("movie_name")
    private String movieId;
    private LocalDateTime date;
    private BigDecimal price;
}
