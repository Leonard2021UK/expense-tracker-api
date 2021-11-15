package com.codercampus.api.exception;

public class ResourceNotCreatedException extends CustomException{

    private ResourceNotCreatedException(String message) {
        super(message);
    }

    public static ResourceNotCreatedException createWith(String message) {
        return new ResourceNotCreatedException(message);
    }
}
