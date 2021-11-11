package com.codercampus.api.exception;

import com.codercampus.api.model.error.ResourceError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import org.springframework.http.HttpHeaders;
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
    @ExceptionHandler({CategoryNotFoundException.class})
    public final ResponseEntity<ResourceError> handleException(Exception ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();

        LOGGER.error("Handling " + ex.getClass().getSimpleName() + " due to " + ex.getMessage());

        if (ex instanceof CategoryNotCreatedException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            CategoryNotCreatedException catNotCreatedException = (CategoryNotCreatedException) ex;

            return handleCategoryNotCreated(catNotCreatedException, headers, status, request);

        } else if (ex instanceof CategoryNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            CategoryNotFoundException catNotFoundException = (CategoryNotFoundException) ex;

            return handleCategoryNotFound(catNotFoundException, headers, status, request);

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
     * Customize the response for CategoryNotFound.
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ResourceError> handleCategoryNotCreated(CategoryNotCreatedException ex,
                                                                   HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {

        List<String> errors = Collections.singletonList(ex.getMessage());

        return handleExceptionInternal(ex, new ResourceError(errors), headers, status, request);
    }

    /**
     * Customize the response for CategoryNotFound.
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ResourceError> handleCategoryNotFound(CategoryNotFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {

        List<String> errors = Collections.singletonList(ex.getMessage());

        return handleExceptionInternal(ex, new ResourceError(errors), headers, status, request);
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
    protected ResponseEntity<ResourceError> handleExceptionInternal(Exception ex, ResourceError body,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}
