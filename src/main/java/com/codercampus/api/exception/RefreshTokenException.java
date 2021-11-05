package com.codercampus.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * Custom exception which is sent back to the client as a 403 FORBIDDEN
 * in case the refresh token is invalid
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RefreshTokenException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
