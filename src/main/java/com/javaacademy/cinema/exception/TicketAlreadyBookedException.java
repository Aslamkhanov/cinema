package com.javaacademy.cinema.exception;

public class TicketAlreadyBookedException extends Exception {
    public TicketAlreadyBookedException(String message) {
        super(message);
    }
}
