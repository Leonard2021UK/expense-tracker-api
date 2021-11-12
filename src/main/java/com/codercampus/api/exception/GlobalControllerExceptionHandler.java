package com.codercampus.api.exception;

import com.codercampus.api.model.error.Error;
import com.codercampus.api.model.error.ErrorCollection;
import com.codercampus.api.model.error.Violation;
import com.codercampus.api.model.error.ennum.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            CategoryNotFoundByNameException.class,
            CategoryNotCreatedException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            NumberFormatException.class
    })
    public final ResponseEntity<ErrorCollection<?>> handleException(Exception ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();

        LOGGER.error("Handling " + ex.getClass().getSimpleName() + " due to " + ex.getMessage());

        if (ex instanceof CategoryNotCreatedException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            CategoryNotCreatedException categoryNCE = (CategoryNotCreatedException) ex;

            return handleCategoryNotCreated(categoryNCE, headers, status, request);

        } else if (ex instanceof CategoryNotFoundByNameException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            CategoryNotFoundByNameException categoryNFE = (CategoryNotFoundByNameException) ex;

            return handleCategoryNotFound(categoryNFE, headers, status, request);

        }else if (ex instanceof ConstraintViolationException) {
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
     * Customize the response for CategoryNotCreated
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorCollection<?>> handleCategoryNotCreated(CategoryNotCreatedException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {

        List<String> errors = Collections.singletonList(ex.getMessage());



        return handleExceptionInternal(ex, new ErrorCollection<>(errors), headers, status, request);
    }

    /**
     * Customize the response for CategoryNotFound.
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorCollection<?>> handleCategoryNotFound(CategoryNotFoundByNameException ex,
                                                                        HttpHeaders headers, HttpStatus status,
                                                                        WebRequest request) {

        List<String> errors = Collections.singletonList(ex.getMessage());

        return handleExceptionInternal(ex, new ErrorCollection<>(errors), headers, status, request);
    }

    /**
     * Customize the response for ConstraintViolationException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorCollection<?>> handleConstraintViolation(ConstraintViolationException ex,
                                                                           HttpHeaders headers, HttpStatus status,
                                                                           WebRequest request) {

        ErrorCollection<Violation> errorResponse = new ErrorCollection<>();
        for(ConstraintViolation<?> violation:ex.getConstraintViolations()){
            errorResponse.getErrors().add(new Violation(violation.getRootBeanClass().getName(), ErrorType.INVALID_DATA.name(),violation.getPropertyPath().toString(),violation.getMessage()));
        }

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Customize the response for MethodArgumentNotValidException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorCollection<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                              HttpHeaders headers, HttpStatus status,
                                                                              WebRequest request) {
        ErrorCollection<Violation> errorResponse = new ErrorCollection<>();
        for (FieldError error:ex.getBindingResult().getFieldErrors()){
            errorResponse.getErrors().add(new Violation(ex.getClass().getSimpleName(), ErrorType.INVALID_DATA.name(),error.getField(),error.getDefaultMessage()));
        }

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Customize the response for MethodArgumentTypeMismatchException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorCollection<?>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                                  HttpHeaders headers, HttpStatus status,
                                                                              WebRequest request) {
        List<Error> errors = Collections.singletonList(new Error(ex.getMessage()));
        ErrorCollection<?> errorC = new ErrorCollection<>(errors);
        errorC.setMessage(ex.getClass().getName());
        return handleExceptionInternal(ex, errorC, headers, status, request);
    }

    /**
     * Customize the response for NumberFormatException
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorCollection<?>> handleNumberFormat(NumberFormatException ex,
                                                                                  HttpHeaders headers, HttpStatus status,
                                                                                  WebRequest request) {
        List<Error> errors = Collections.singletonList(new Error(ex.getMessage()));
        return handleExceptionInternal(ex, new ErrorCollection<>(errors), headers, status, request);
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
    protected ResponseEntity<ErrorCollection<?>> handleExceptionInternal(Exception ex, ErrorCollection<?> body,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}
