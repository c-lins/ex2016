package org.c.lins.auth.config.security.jwt.filter;

import com.nimbusds.jose.JWSObject;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.json.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public final class JWTOrFormAuthenticationFilter extends AuthenticatingFilter {

    public static final String USER_ID = "username";
    public static final String PASSWORD = "password";

    protected static final String AUTHORIZATION_HEADER = "Authorization";

    public JWTOrFormAuthenticationFilter() {
        setLoginUrl(DEFAULT_LOGIN_URL);
    }

    @Override
    public void setLoginUrl(String loginUrl) {
        String previous = getLoginUrl();
        if (previous != null) {
            this.appliedPaths.remove(previous);
        }
        super.setLoginUrl(loginUrl);
        this.appliedPaths.put(getLoginUrl(), null);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean loggedIn = false;

        if (isLoginRequest(request, response) || isLoggedAttempt(request, response)) {
            loggedIn = executeLogin(request, response);
        }

        if (!loggedIn) {
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return loggedIn;
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws IOException {

            System.out.println("===========---"+getAuthzHeader(request));
        if (isLoginRequest(request, response)) {
            String json = IOUtils.toString(request.getInputStream());
            String username = request.getParameter(USER_ID);
            String password = request.getParameter(PASSWORD);
            if (json != null && !json.isEmpty()) {

                JSONObject jsonObj = new JSONObject(json);

                username = (String)jsonObj.get(USER_ID);
                password = (String)jsonObj.get(PASSWORD);
            }
            return new UsernamePasswordToken(username, password);
        }

        if (isLoggedAttempt(request, response)) {
            String jwtToken = getAuthzHeader(request);
            if (jwtToken != null) {
                return createToken(jwtToken);
            }
        }

        return new UsernamePasswordToken();
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return false;
    }

    protected boolean isLoggedAttempt(ServletRequest request, ServletResponse response) {
        String authzHeader = getAuthzHeader(request);
        return authzHeader != null;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(AUTHORIZATION_HEADER);
    }

    public JWTAuthenticationToken createToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            String decrypted = jwsObject.getPayload().toString();

            JSONObject jsonObj = new JSONObject(decrypted);
            Object o = jsonObj.get("sub");

                return new JWTAuthenticationToken(o, token);



        } catch (ParseException ex) {
            throw new AuthenticationException(ex);
        }

    }

}
