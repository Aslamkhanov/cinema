package com.javaacademy.cinema.exception;

public class TicketAlreadyBookedException extends RuntimeException {
    public TicketAlreadyBookedException() {
        super("Билет уже куплен");
    }
}
