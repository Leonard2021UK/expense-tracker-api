package com.codercampus.api.exception;

public class ResourceAlreadyExistException extends Exception{
    private ResourceAlreadyExistException(String message) {
        super(message);
    }

    public static ResourceAlreadyExistException create(String name) {
        String message = "Resource with name " + name + " already exists!";
        return new ResourceAlreadyExistException(message);
    }
}
