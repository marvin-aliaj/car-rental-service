package com.spring.jdbc.car.rental.exception;

public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }

    public CustomException() {
        super("An error occurred");
    }
}