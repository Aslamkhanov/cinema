package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Дто Сессии")
public class SessionDto {
    @Schema(description = "id сессии")
    private Integer id;

    @Schema(description = "id фильма")
    @JsonProperty("movie_id")
    private Integer movieId;

    @Schema(description = "дата")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date_time")
    private LocalDateTime dateTime;

    @Schema(description = "цена")
    private BigDecimal price;
}
