package com.codercampus.api.exception;

public class ResourceHasReferenceException extends Exception{
    private ResourceHasReferenceException(String message) {
        super(message);
    }

    public static ResourceHasReferenceException createWith(String name) {
        String message = name + " ,still referenced by another resource!";
        return new ResourceHasReferenceException(message);
    }
}
