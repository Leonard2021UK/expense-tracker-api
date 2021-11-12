package com.codercampus.api.model.error;

import com.codercampus.api.model.error.ennum.ErrorType;

import java.util.UUID;

public class Error {
    private final UUID errorUniqueID;
    protected String source;
    protected String message;


    protected ErrorType type;


    public Error(){
        this.errorUniqueID = UUID.randomUUID();
    };

    public Error(String message) {
        this.errorUniqueID = UUID.randomUUID();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }

    public UUID getErrorUniqueID() {
        return errorUniqueID;
    }

}
