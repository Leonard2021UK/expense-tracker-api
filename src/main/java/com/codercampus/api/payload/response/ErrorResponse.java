package com.codercampus.api.payload.response;

import java.util.ArrayList;
import java.util.List;

public  class ErrorResponse<T> {

    private String message;
    List<T> errors = new ArrayList<>();

    public ErrorResponse() {}

    public ErrorResponse(List<T> errors){
        this.errors = errors;
    }

    public List<T> getErrors() {
        return errors;
    }

    public void setErrors(List<T> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
