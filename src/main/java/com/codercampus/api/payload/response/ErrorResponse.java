package com.codercampus.api.payload.response;

import java.util.UUID;

public class ErrorResponse {

    private UUID errorUniqueID;
    private String message;


    public ErrorResponse(String message) {
        this.errorUniqueID = UUID.randomUUID();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
