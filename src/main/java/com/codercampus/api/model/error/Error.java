package com.codercampus.api.model.error;

public class Error {

    protected String message;

    public Error(){};

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
