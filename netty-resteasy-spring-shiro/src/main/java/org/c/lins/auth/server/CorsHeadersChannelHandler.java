package org.c.lins.auth.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jboss.resteasy.plugins.server.netty.NettyHttpRequest;

/**
 * Created by liuzh on 16-1-23.
 */
public class CorsHeadersChannelHandler extends SimpleChannelInboundHandler<NettyHttpRequest> {
    protected void channelRead0(ChannelHandlerContext ctx, NettyHttpRequest request) throws Exception {
        request.getResponse().getOutputHeaders().add("Access-Control-Allow-Origin", "*");
        request.getResponse().getOutputHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        request.getResponse().getOutputHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Content-Type, Content-Length");

        ctx.fireChannelRead(request);
    }
}
