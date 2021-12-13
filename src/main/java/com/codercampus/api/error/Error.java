package com.codercampus.api.error;

import com.codercampus.api.error.ennum.EErrorType;

import java.util.UUID;

public class Error {
    private final UUID errorUniqueID;
    protected String message;
    protected EErrorType type;
    protected String detail;
    protected Object body;


    public Error(){
        this.errorUniqueID = UUID.randomUUID();
    };

    public Error(String message) {
        this.errorUniqueID = UUID.randomUUID();
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EErrorType getType() {
        return type;
    }

    public void setType(EErrorType type) {
        this.type = type;
    }

    public UUID getErrorUniqueID() {
        return errorUniqueID;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
