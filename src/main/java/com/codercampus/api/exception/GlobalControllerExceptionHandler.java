package com.codercampus.api.exception;

import com.codercampus.api.error.Error;
import com.codercampus.api.error.ennum.EErrorType;
import com.codercampus.api.payload.response.ErrorResponse;
import com.codercampus.api.error.Violation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.WebUtils;

import org.springframework.http.HttpHeaders;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Provides handling for exceptions throughout this service.
     *
     * @param ex The target exception
     * @param request The current request
     */
    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            NumberFormatException.class,
            EmptyResultDataAccessException.class,
            NoSuchElementException.class,
            ResourceNotFoundException.class,
            ResourceNotUpdatedException.class,
            ResourceAlreadyExistException.class,
            ResourceHasReferenceException.class,
            RefreshTokenException.class
    })
    public final ResponseEntity<ErrorResponse<?>> handleException(Exception ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();

        LOGGER.error("Handling " + ex.getClass().getSimpleName() + " due to " + ex.getMessage());

        if (ex instanceof ConstraintViolationException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ConstraintViolationException constraintVE = (ConstraintViolationException) ex;

            return handleConstraintViolation(constraintVE, headers, status, request);

        }else if (ex instanceof MethodArgumentNotValidException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            MethodArgumentNotValidException methodANVE = (MethodArgumentNotValidException) ex;

            return handleMethodArgumentNotValid(methodANVE, headers, status, request);

        }else if (ex instanceof MethodArgumentTypeMismatchException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            MethodArgumentTypeMismatchException methodATME = (MethodArgumentTypeMismatchException) ex;

            return handleMethodArgumentTypeMismatch(methodATME, headers, status, request);

        }else if (ex instanceof NumberFormatException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            NumberFormatException numberFE = (NumberFormatException) ex;

            return handleNumberFormat(numberFE, headers, status, request);

        }else if (ex instanceof ResourceNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ResourceNotFoundException resourceNFE = (ResourceNotFoundException) ex;

            return handleResourceNotFound(resourceNFE, headers, status, request);

        }else if (ex instanceof ResourceNotUpdatedException) {
            HttpStatus status = HttpStatus.NOT_MODIFIED;
            ResourceNotUpdatedException resourceNUE = (ResourceNotUpdatedException) ex;

            return handleResourceNotUpdated(resourceNUE, headers, status, request);

        }else if (ex instanceof ResourceAlreadyExistException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ResourceAlreadyExistException resourceAEE = (ResourceAlreadyExistException) ex;

            return handleResourceAlreadyExists(resourceAEE, headers, status, request);

        }else if(ex instanceof ResourceHasReferenceException){
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ResourceHasReferenceException resourceHRE = (ResourceHasReferenceException) ex;

            return handleResourceHasReference(resourceHRE, headers, status, request);
        }else if(ex instanceof RefreshTokenException){
            HttpStatus status = HttpStatus.FORBIDDEN;
            RefreshTokenException refreshTokenException = (RefreshTokenException) ex;

            return handleRefreshTokenException(refreshTokenException, headers, status, request);
        }

        else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Unknown exception type: " + ex.getClass().getName());
            }

            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }


    /**
     * Customize the response for ConstraintViolationException
     * This method handles invalid form field data when using request parameter annotations
     * eg.:@RequestParam("param") @Min(5) int param
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>> handleConstraintViolation(ConstraintViolationException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {



        ErrorResponse<Violation> errorResponse = new ErrorResponse<>();
        for(ConstraintViolation<?> violation:ex.getConstraintViolations()){
            Violation customViolation = new Violation(violation.getPropertyPath().toString(),violation.getMessage());
            customViolation.setType(EErrorType.INVALID_DATA);
            errorResponse.getErrors().add(customViolation);        }

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Customize the response for NumberFormatException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>>handleResourceHasReference(ResourceHasReferenceException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        Error error = new Error(ex.getMessage());
        error.setType(EErrorType.RESOURCE_HAS_REFERENCE);
        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Customize the response for NumberFormatException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>>handleRefreshTokenException(RefreshTokenException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        Error error = new Error(ex.getMessage());
        error.setType(EErrorType.REFRESH_TOKEN_EXPIRED);
        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }
    /**
     * Customize the response for MethodArgumentNotValidException
     * This method handles invalid form field data
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                            HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        ErrorResponse<Violation> errorResponse = new ErrorResponse<>();
        for (FieldError error:ex.getBindingResult().getFieldErrors()){
            Violation violation = new Violation(error.getField(),error.getDefaultMessage());
            violation.setType(EErrorType.INVALID_DATA);
            errorResponse.getErrors().add(violation);
        }

        errorResponse.setMessage("Invalid field constraints");

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Customize the response for MethodArgumentTypeMismatchException
     * This method handles invalid request param conversion
     * eg.: id cannot be converted to numeric value ("58gtg" to Long)
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                                HttpHeaders headers, HttpStatus status,
                                                                                WebRequest request) {
        Error error = new Error(getDetailedMessage(ex));

        error.setType(EErrorType.INVALID_RESOURCE_ID);

        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage("The requested resource ID is invalid");

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Customize the response for NumberFormatException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>> handleNumberFormat(NumberFormatException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<Error> errors = Collections.singletonList(new Error(ex.getMessage()));
        return handleExceptionInternal(ex, new ErrorResponse<>(errors), headers, status, request);
    }


    /**
     * Customize the response for NumberFormatException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>>handleResourceNotFound(ResourceNotFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
        Error error = new Error(ex.getMessage());
        error.setType(EErrorType.RESOURCE_NOT_FOUND);
        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }


    /**
     * Customize the response for NumberFormatException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>>handleResourceNotUpdated(ResourceNotUpdatedException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        Error error = new Error(ex.getMessage());
        error.setType(EErrorType.RESOURCE_NOT_UPDATED);
        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Customize the response for NumberFormatException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorResponse<?>>handleResourceAlreadyExists(ResourceAlreadyExistException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        System.out.println(request);
        Error error = new Error(ex.getMessage());
        error.setType(EErrorType.RESOURCE_ALREADY_EXISTS);
        error.setDetail("The name already saved in the database.Choose another!");
        List<Error> errors = Collections.singletonList(error);

        ErrorResponse<?> errorResponse = new ErrorResponse<>(errors);

        errorResponse.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }





    /**
     * A single place to customize the response body of all Exception types.
     *
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     *
     * @param ex The exception
     * @param body The body for the response
     * @param headers The headers for the response
     * @param status The response status
     * @param request The current request
     */
    protected ResponseEntity<ErrorResponse<?>> handleExceptionInternal(Exception ex, ErrorResponse<?> body,
                                                                       HttpHeaders headers, HttpStatus status,
                                                                       WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }

    protected String getDetailedMessage(Exception ex){

        final String message = ex.getMessage();
        if (message == null) {
            return "";
        }
        final int tailIndex = StringUtils.indexOf(message, "; nested exception is");
        if (tailIndex == -1) {
            return message;
        }
        return StringUtils.left(message, tailIndex);

    }
}
