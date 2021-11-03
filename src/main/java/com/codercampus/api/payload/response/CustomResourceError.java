package com.codercampus.api.payload.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class CustomResourceError {
    private final UUID errorUniqueID;
    private final LocalDateTime dateTime;
    private String message;
    private String errorTypeId;
    private Exception exception;

    public CustomResourceError(Exception exception) {
        this.errorUniqueID = UUID.randomUUID();
        this.dateTime = LocalDateTime.now();
        this.exception = exception;
    }


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

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

}
