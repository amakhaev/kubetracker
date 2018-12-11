package com.tracker.domain.token;

/**
 * Provides the service to work with token
 */
public interface TokenService {

    /**
     * Provides the default token service instance
     */
    TokenService INSTANCE = new TokenServiceImpl();

    /**
     * Gets the token model
     */
    TokenModel getToken();

}
