package com.codercampus.api.exception;

public class ResourceNotUpdatedException extends CustomException{

    private ResourceNotUpdatedException(String message) {
        super(message);
    }

    public static ResourceNotUpdatedException createWith(String message) {
        return new ResourceNotUpdatedException(message);
    }
}
