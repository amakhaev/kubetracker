package com.tracker.domain.common.exception;

/**
 * Occurs when for some reason an operation can't be made.
 */
public class OperationDeniedException extends RuntimeException {

    /**
     * Initializes a new {@link OperationDeniedException} instance.
     *
     * @param message - error description message.
     */
    public OperationDeniedException(String message) {
        super(message);
    }
}
