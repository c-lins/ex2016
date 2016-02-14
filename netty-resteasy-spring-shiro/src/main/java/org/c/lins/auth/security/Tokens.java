package org.c.lins.auth.security;

import org.c.lins.auth.security.jwt.TokenResponse;

import java.security.SecureRandom;

/**
 * Created by lins on 16-1-19.
 */
public interface Tokens {
    String getIssuer();

    byte[] getSharedKey();

    String createToken(Object userId);

    boolean validateToken(String token);

    default byte[] generateSharedKey() {
        SecureRandom random = new SecureRandom();
        byte[] sharedKey = new byte[32];
        random.nextBytes(sharedKey);
        return sharedKey;
    }

    default long getExpirationDate() {
        return 1000 * 60 * 60 * 24 * 5;
    }

    default TokenResponse createToken(ShiroUser user) {
        TokenResponse response = new TokenResponse(user, createToken(user.getPrincipal()));
        return response;
    }
}
