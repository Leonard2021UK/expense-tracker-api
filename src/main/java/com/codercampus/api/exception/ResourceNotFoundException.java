package com.codercampus.api.exception;

public class ResourceNotFoundException extends Exception{

    private ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException createWith(String message) {
        return new ResourceNotFoundException(message);
    }
}
