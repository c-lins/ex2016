package org.c.lins.auth.server;

import com.nimbusds.jose.JWSObject;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.util.AttributeKey;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.c.lins.auth.security.jwt.filter.JWTAuthenticationToken;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.resteasy.plugins.server.netty.NettyHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.ParseException;
import java.util.Map;

import static org.jboss.resteasy.util.HttpHeaderNames.AUTHORIZATION;

/**
 * <a href="http://shiro.apache.org">Shiro</a> handler exposing the {@link Subject}
 * to following handlers in the pipeline.
 *
 * To use it, add it in your pipeline after the {@link io.netty.handler.codec.http.HttpRequestDecoder}.
 */
//@ChannelHandler.Sharable
//@SuppressWarnings({"PMD.AvoidPrefixingMethodParameters", "PMD.DataflowAnomalyAnalysis"})
public class ShiroHandler extends SimpleChannelInboundHandler<NettyHttpRequest> {

    public static final String USER_ID = "username";
    public static final String PASSWORD = "password";

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    private final SecurityManager securityManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroHandler.class);
    public static final String BEARER = "Bearer ";
    public static final AttributeKey<Subject> ATTR_SUBJECT = AttributeKey.valueOf("SHIRO_SUBJECT");

    public ShiroHandler(SecurityManager securityManager) {
        super();
        if (securityManager == null) {
            throw new NullPointerException("Shiro security manager can't be null");
        }
        this.securityManager = securityManager;
        SecurityUtils.setSecurityManager(securityManager);
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof NettyHttpRequest) {
//            NettyHttpRequest httpMessage = (NettyHttpRequest) msg;
//            LOGGER.debug("Intercepting {} request on '{}'", httpMessage.getHttpMethod(), httpMessage.getUri());
//            SocketAddress socketAddress = ctx.channel().remoteAddress();
//            String remoteHost;
//            if (socketAddress instanceof InetSocketAddress) {
//                remoteHost = ((InetSocketAddress) socketAddress).getHostString();
//            } else {
//                remoteHost = "";
//
//            }
////            if (httpMessage.getHttpHeaders().getHeaderString(AUTHORIZATION)!=null) {
////                Subject subject = new Subject.Builder(securityManager)
////                        .host(remoteHost)
////                        .buildSubject();
////                String authorization = httpMessage.getHttpHeaders().getHeaderString(AUTHORIZATION);
////                if (authorization.startsWith(BEARER)) {
////                    String bearer = authorization.substring(BEARER.length());
////                    OAuth2Token token = new OAuth2Token(bearer);
////                    try {
////                        subject.login(token);
////                        ctx.channel().attr(ATTR_SUBJECT).set(subject);
////                    } catch (AuthenticationException e) {
////                        LOGGER.error("Can't authenticate user with OAuth2 token '{}'", bearer);
////                        String errorMessage = String.format("OAuth2 token '%s' is not valid.", bearer);
////                        HttpResponse httpResponse = new DefaultFullHttpResponse(
////                                HttpVersion.HTTP_1_1,
////                                HttpResponseStatus.UNAUTHORIZED,
////                                Unpooled.copiedBuffer(errorMessage,
////                                        Charset.defaultCharset()));
////                        httpResponse.headers().set(CONTENT_LENGTH, errorMessage.length());
////                        httpResponse.headers().set(CONTENT_TYPE, MediaType.TEXT_PLAIN);
////                        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
////                        return;
////                    }
////                }
////            }else{
//                AuthenticationToken token = createToken(httpMessage);
//                if(token == null) {
//                    String e1 = "createToken method implementation returned null. A valid non-null AuthenticationToken must be created in order to execute a login attempt.";
//                    throw new IllegalStateException(e1);
//                } else {
//                    try {
//                        Subject e = SecurityUtils.getSubject();
//                        e.login(token);
//                        //return this.onLoginSuccess(token, e, request, response);
//                    } catch (AuthenticationException var5) {
//                        var5.printStackTrace();
//                        //return this.onLoginFailure(token, var5, request, response);
//                    }
//                }
////            }
//        } else {
//            LOGGER.debug("Ignoring unsupported message {}", msg);
//        }
//        super.channelRead(ctx, msg);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyHttpRequest msg) throws Exception {
        if (msg instanceof NettyHttpRequest) {
            NettyHttpRequest httpMessage = (NettyHttpRequest) msg;
            LOGGER.debug("Intercepting {} request on '{}'", httpMessage.getHttpMethod(), httpMessage.getUri());
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            String remoteHost;
            if (socketAddress instanceof InetSocketAddress) {
                remoteHost = ((InetSocketAddress) socketAddress).getHostString();
            } else {
                remoteHost = "";

            }
//            if (httpMessage.getHttpHeaders().getHeaderString(AUTHORIZATION)!=null) {
//                Subject subject = new Subject.Builder(securityManager)
//                        .host(remoteHost)
//                        .buildSubject();
//                String authorization = httpMessage.getHttpHeaders().getHeaderString(AUTHORIZATION);
//                if (authorization.startsWith(BEARER)) {
//                    String bearer = authorization.substring(BEARER.length());
//                    OAuth2Token token = new OAuth2Token(bearer);
//                    try {
//                        subject.login(token);
//                        ctx.channel().attr(ATTR_SUBJECT).set(subject);
//                    } catch (AuthenticationException e) {
//                        LOGGER.error("Can't authenticate user with OAuth2 token '{}'", bearer);
//                        String errorMessage = String.format("OAuth2 token '%s' is not valid.", bearer);
//                        HttpResponse httpResponse = new DefaultFullHttpResponse(
//                                HttpVersion.HTTP_1_1,
//                                HttpResponseStatus.UNAUTHORIZED,
//                                Unpooled.copiedBuffer(errorMessage,
//                                        Charset.defaultCharset()));
//                        httpResponse.headers().set(CONTENT_LENGTH, errorMessage.length());
//                        httpResponse.headers().set(CONTENT_TYPE, MediaType.TEXT_PLAIN);
//                        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
//                        return;
//                    }
//                }
//            }else{
            AuthenticationToken token = createToken(httpMessage);
            if(token == null) {
                String e1 = "createToken method implementation returned null. A valid non-null AuthenticationToken must be created in order to execute a login attempt.";
                //throw new IllegalStateException(e1);
                ctx.fireChannelRead(httpMessage);
            } else {
                try {
                    //securityManager.login(token)
                    Subject e = SecurityUtils.getSubject();
                    e.login(token);

                    ctx.fireChannelRead(httpMessage);
                    //return this.onLoginSuccess(token, e, request, response);
                } catch (AuthenticationException var5) {
                    var5.printStackTrace();
                    //return this.onLoginFailure(token, var5, request, response);
                }
            }
//            }
        } else {
            LOGGER.debug("Ignoring unsupported message {}", msg);
        }
//        super.channelRead(ctx, msg);
    }

//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        super.write(ctx, msg, promise);
//        if (msg instanceof HttpMessage) {
//            Subject subject = ctx.channel().attr(ATTR_SUBJECT).getAndRemove();
//            if (subject != null) {
//                subject.logout();
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        LOGGER.error("Unexpected error", cause);
//        cause.printStackTrace();
//        ctx.close();
//    }

    protected AuthenticationToken createToken(NettyHttpRequest request) throws IOException {

        System.out.println("===========---"+getAuthzHeader(request));
        if (!request.getHttpHeaders().getRequestHeaders().containsKey(AUTHORIZATION)) {
//            String id = (String) request.get("id");
//            Map<String, String> parmMap = new RequestParser(request).parse(); // 将GET, POST所有请求参数转换成Map对象
////            String json = IOUtils.toString(request.getgetInputStream());
////            String username = request.getParameter(USER_ID);
////            String password = request.getParameter(PASSWORD);
//            String json = null,username = "yyw01",password="123456";
//            if (json != null && !json.isEmpty()) {
//
//                JSONObject jsonObj = null;
//                try {
//                    jsonObj = new JSONObject(json);
//                    username = (String)jsonObj.get(USER_ID);
//                    password = (String)jsonObj.get(PASSWORD);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            return new UsernamePasswordToken(username, password);
            return null;
        }
        if (isLoggedAttempt(request)) {
            String jwtToken = getAuthzHeader(request);
            if (jwtToken != null) {
                return createToken(jwtToken);
            }
        }

        return new UsernamePasswordToken();
    }

    protected boolean isLoggedAttempt(NettyHttpRequest request) {
        String authzHeader = getAuthzHeader(request);
        return authzHeader != null;
    }

    protected String getAuthzHeader(NettyHttpRequest request) {
        return request.getHttpHeaders().getHeaderString(AUTHORIZATION_HEADER);
    }

    public JWTAuthenticationToken createToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            String decrypted = jwsObject.getPayload().toString();

            JSONObject jsonObj = new JSONObject(decrypted);
            Object o = jsonObj.get("sub");


            return new JWTAuthenticationToken(o, token);



        } catch (ParseException|JSONException ex) {
            throw new AuthenticationException(ex);
        }

    }
}
