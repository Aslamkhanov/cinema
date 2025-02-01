package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketRequest {
    @JsonProperty("session_id")
    private Integer sessionId;
    @JsonProperty("place_name")
    private String placeName;
}
