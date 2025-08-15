package com.invoice.planner.utils;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
