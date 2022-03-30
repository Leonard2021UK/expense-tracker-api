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
public class GlobalErrorHandlerService {

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

    /**
     * Handles error response when the requested resource cannot be updated.
     * @return a {@code ResponseEntity} instance
     */
    public ResponseEntity<List<ErrorResponse<?>>> handleResourceNotUpdatedError(String resourceName,Object body) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error error = new Error("Resource with name: " + resourceName + " was not updated!");
        error.setBody(body);
        error.setType(EErrorType.RESOURCE_NOT_UPDATED);

        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage("Resource was not updated!");

        return new ResponseEntity<>(Collections.singletonList(errorResponse), headers, status);

    }

    /**
     * Handles error response when the requested resource cannot be deleted.
     * @return a {@code ResponseEntity} instance
     */
    public ResponseEntity<List<ErrorResponse<?>>> handleResourceNotDeletedError(String resourceName,Object body) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_MODIFIED;
        Error error = new Error("Resource with name: " + resourceName + " was not deleted!");
        error.setBody(body);
        error.setType(EErrorType.RESOURCE_NOT_DELETED);

        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage("Resource was not deleted!");

        return new ResponseEntity<>(Collections.singletonList(errorResponse), headers, status);

    }

    /**
     * Handles error response when the requested resource already exists.
     * @return a {@code ResponseEntity} instance
     */
    public ResponseEntity<List<?>> handleResourceAlreadyExistError(String resourceName,Object body) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error error = new Error("Resource with name: " + resourceName + " already exists!");
        error.setBody(body);

        error.setType(EErrorType.RESOURCE_ALREADY_EXISTS);

        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage("Resource already exists!");

        return new ResponseEntity<>(Collections.singletonList(errorResponse), headers, status);
    }

    /**
     * Handles error response when the requested resource was not found.
     * @return a {@code ResponseEntity} instance
     */
    public ResponseEntity<ErrorResponse<?>> handleResourceNotFoundError(String resourceName,Object body) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;
        Error error = new Error("Resource with id: " + resourceName + " was not found!");

        error.setType(EErrorType.RESOURCE_NOT_FOUND);
        error.setBody(body);

        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage("Requested resource has not been found!");

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    /**
     * Handles error response when the requested resource was not found.
     * @return a {@code ResponseEntity} instance
     */
    public ResponseEntity<ErrorResponse<?>> handleResourceNotEmptyError(String resourceName,Object body) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_MODIFIED;
        Error error = new Error("Resource with id: " + resourceName + " is not empty!");

        error.setType(EErrorType.RESOURCE_NOT_UPDATED);
        error.setBody(body);

        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage("Requested resource was not deleted!");

        return new ResponseEntity<>(errorResponse, headers, status);
    }
}
