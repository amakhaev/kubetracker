package com.tracker.domain.common.exception;

/**
 * Occurs when entity with given data already exist in database.
 */
public class DuplicatedEntityException extends RuntimeException {

    /**
     * Initializes a new {@link DuplicatedEntityException} instance.
     *
     * @param message - error description message.
     */
    public DuplicatedEntityException(String message) {
        super(message);
    }

    /**
     * Initializes a new {@link DuplicatedEntityException} instance with the specified
     * description message and cause.
     *
     * @param message - error description message.
     * @param cause - the original cause of the exception.
     */
    public DuplicatedEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
