package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Дто запроса на билет")
public class TicketRequestDto {
    @Schema(description = "id сессии")
    @JsonProperty("session_id")
    private Integer sessionId;

    @Schema(description = "место в билете")
    @JsonProperty("place_name")
    private String placeName;
}
