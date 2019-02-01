package com.tracker.domain.common.exception;

/**
 * Provides the Unauthorized exception
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Initialize new instance of {@link UnauthorizedException}.
     *
     * @param message - the detail message.
     */
    public UnauthorizedException(String message) {
        super(message);
    }

}
