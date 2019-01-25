package com.tracker.domain.common.exception;

/**
 * Occurs when requested entity can not be found.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Default constructor.
     *
     * @param message - description.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
