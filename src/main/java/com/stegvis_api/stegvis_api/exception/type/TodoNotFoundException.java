package com.stegvis_api.stegvis_api.exception.type;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String message) {
        super(message);
    }
}