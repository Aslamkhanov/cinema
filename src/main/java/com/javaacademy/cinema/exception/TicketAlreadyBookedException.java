package com.javaacademy.cinema.exception;

public class TicketAlreadyBookedException extends RuntimeException {
    public TicketAlreadyBookedException(String message) {
        super(message);
    }
}
