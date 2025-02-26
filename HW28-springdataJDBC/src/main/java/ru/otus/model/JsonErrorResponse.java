package ru.otus.model;

public class JsonErrorResponse {
    private String message;
    private String details;

    public JsonErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }

    // Геттеры
    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
