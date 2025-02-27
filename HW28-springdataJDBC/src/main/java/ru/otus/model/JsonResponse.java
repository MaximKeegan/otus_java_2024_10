package ru.otus.model;

public class JsonResponse {
    private String message;
    private String details;

    public JsonResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
