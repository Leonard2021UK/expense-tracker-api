package com.codercampus.api.payload.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class CustomResourceError {
    private final UUID errorUniqueID;
    private final LocalDateTime dateTime;

    public CustomResourceError() {
        this.errorUniqueID = UUID.randomUUID();
        this.dateTime = LocalDateTime.now();
    }

    private String message;
    private String errorTypeId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorTypeId() {
        return errorTypeId;
    }

    public void setErrorTypeId(String errorTypeId) {
        this.errorTypeId = errorTypeId;
    }
}
