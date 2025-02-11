package com.javaacademy.cinema.exception;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException() {
        super("Нет прав доступа, авторизуйтесь как администратор");
    }
}
