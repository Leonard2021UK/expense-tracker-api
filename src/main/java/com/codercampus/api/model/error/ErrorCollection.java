package com.codercampus.api.model.error;

import java.util.ArrayList;
import java.util.List;

public  class ErrorCollection<T> {

    private String message;
    List<T> errors = new ArrayList<>();

    public ErrorCollection() {}

    public ErrorCollection(List<T> errors){
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
