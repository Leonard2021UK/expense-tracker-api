package com.codercampus.api.error;

public class Violation extends Error{

    private final String fieldName;

    public Violation(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }


}
