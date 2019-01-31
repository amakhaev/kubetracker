package com.tracker.domain.common.exception;

/**
 * Occurs when request has invalid params.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Initialize new instance of {@link BadRequestException}.
     *
     * @param message - the detail message.
     */
    public BadRequestException(String message) {
        super(message);
    }

}
