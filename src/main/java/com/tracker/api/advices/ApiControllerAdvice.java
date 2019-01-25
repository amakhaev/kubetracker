package com.tracker.api.advices;

import com.tracker.domain.common.exception.BadRequestException;
import com.tracker.domain.common.exception.DuplicatedEntityException;
import com.tracker.domain.common.exception.EntityNotFoundException;
import com.tracker.domain.common.exception.OperationDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.ConstraintViolationException;

/**
 * API global advice.
 */
@Slf4j
@RestControllerAdvice
@ApiIgnore
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handles DuplicateEntityException thrown by REST API methods.
     *
     * @param ex - exception instance.
     * @param request - request instance.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(DuplicatedEntityException.class)
    public ResponseEntity<Object> handleDuplicatedEntityException(DuplicatedEntityException ex, WebRequest request) {
        log.error("Similar object already exists on {}: {}", request.getContextPath(), ex.getMessage());

        final ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.CONFLICT, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

    /**
     * Handles EntityNotFoundException thrown by REST API methods.
     *
     * @param ex - exception instance.
     * @param request - request instance.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error("EntityNotFoundException was occurred on {}: {}", request.getContextPath(), ex.getMessage());

        final ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.error("Validation was failed on {}: {}", request.getContextPath(), ex.getLocalizedMessage());

        final ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

    /**
     * Handles BadRequestException thrown by REST API methods.
     *
     * @param ex - exception instance.
     * @param request - request instance.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.error("The request contains invalid params: {}: {}", request.getContextPath(), ex.getLocalizedMessage());

        final ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

    /**
     * Handles {@link OperationDeniedException} thrown by the REST API method
     *
     * @param ex - Exception instance.
     * @param request - Request instance.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(OperationDeniedException.class)
    public ResponseEntity<Object> handleOperationDeniedException(OperationDeniedException ex, WebRequest request) {
        log.error("Operation on {} was rejected by server: {}", request.getContextPath(), ex.getMessage());

        final ApiErrorResponse apiError = new ApiErrorResponse(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());

        return super.handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }
}

