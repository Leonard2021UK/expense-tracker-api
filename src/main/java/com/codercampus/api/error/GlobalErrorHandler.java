package com.codercampus.api.error;

import com.codercampus.api.error.ennum.EErrorType;
import com.codercampus.api.payload.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;

@Component
public class GlobalErrorHandler {

    /**
     * Handles error response when the requested resource was not created.
     * @return a {@code ResponseEntity} instance
     */
    public ResponseEntity<ErrorResponse<?>> handleResourceNotCreatedError(String resourceName) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error error = new Error(resourceName + " was not created!");

        error.setType(EErrorType.RESOURCE_NOT_CREATED);

        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage("Resource was not created!");

        return new ResponseEntity<>(errorResponse, headers, status);
    }
}
