package com.tracker.api.advices;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Common API error response object.
 */
@Getter
@Setter
@NoArgsConstructor
class ApiErrorResponse {

    /**
     * Creates new instance of API error response
     * @param status - the HTTP status code related to the error.
     * @param message - the error message occurred.
     */
    ApiErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * The HTTP status code related to the error.
     */
    private HttpStatus status;

    /**
     * The error message occurred.
     */
    private String message;
}

