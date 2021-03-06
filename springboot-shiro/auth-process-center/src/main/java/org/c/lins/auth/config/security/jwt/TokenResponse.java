package org.c.lins.auth.config.security.jwt;

import org.c.lins.auth.config.security.ShiroUser;

public class TokenResponse {

    public TokenResponse() {
    }

    public TokenResponse(ShiroUser user, String token) {
        this.user = user;
        this.token = token;
    }

    private String token;

    private ShiroUser user;

    public String getToken() {
        return token;
    }

    public ShiroUser getUser() {
        return user;
    }

}
