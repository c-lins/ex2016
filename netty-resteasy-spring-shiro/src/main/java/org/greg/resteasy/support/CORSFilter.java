package org.greg.resteasy.support;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lins on 15-12-23.
 */
@Component
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest  req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
//        Serializable sessionId = null;
//        Subject requestSubject = new Subject.Builder().sessionId(sessionId).buildSubject();

        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type,authorization, X-E4M-With,Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
//        response.addHeader(
//                "Access-Control-Allow-Credentials", "true"); // qwest.get(url, null, { withCredentials: true }); //允许跨域设置cookie
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}