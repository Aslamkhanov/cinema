package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Дто ответ билета")
public class TicketResponseDto {
    @Schema(description = "id билета")
    @JsonProperty("ticket_id")
    private Integer ticketId;

    @Schema(description = "место в билете")
    @JsonProperty("place_name")
    private String placeName;

    @Schema(description = "название фильма")
    @JsonProperty("movie_name")
    private String movieName;

    @Schema(description = "Дата сессии")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
}
