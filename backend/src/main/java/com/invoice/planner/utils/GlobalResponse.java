package com.invoice.planner.utils;

import java.time.LocalDateTime;

/**
 * Réponse standardisée pour toutes les API
 * @param <T> Type de données dans la réponse
 */
public class GlobalResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    public GlobalResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public GlobalResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public GlobalResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> GlobalResponse<T> success(String message, T data) {
        return new GlobalResponse<>(true, message, data);
    }

    public static <T> GlobalResponse<T> error(String message) {
        return new GlobalResponse<>(false, message, null);
    }
    
    public static <T> GlobalResponse<T> error(String message, T data) {
        return new GlobalResponse<>(false, message, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
