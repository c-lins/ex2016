package org.greg.resteasy.server;

import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.jboss.resteasy.plugins.server.netty.NettyHttpRequest;
import org.jboss.resteasy.plugins.server.netty.NettyHttpResponse;
import org.jboss.resteasy.spi.DefaultOptionsMethodException;

/**
 * Created by liuzh on 16-1-23.
 */
public class OptionsChannelHandler extends SimpleChannelInboundHandler<NettyHttpRequest> {

    protected void channelRead0(ChannelHandlerContext ctx, NettyHttpRequest request) throws Exception {
//        if (!request.getHttpMethod().equals("OPTIONS")){
            ctx.fireChannelRead(request);
//        }
//
//        NettyHttpResponse response = request.getResponse();
//        response.reset();
//        response.setStatus(200);
//
//        if(!request.getAsyncContext().isSuspended()) {
//            response.finish();
//        }
        //throw new DefaultOptionsMethodException("----",response.getDefaultHttpResponse());

    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        //logger.info("InboundHandler1.channelReadComplete");
//        ctx.flush();
//    }
}
