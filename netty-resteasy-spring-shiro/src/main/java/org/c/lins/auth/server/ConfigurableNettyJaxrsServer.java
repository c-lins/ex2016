package org.c.lins.auth.server;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.netty.*;

/**
 * exposing bootstrap config
 * 
 * @author Gregory
 * 
 */
public class ConfigurableNettyJaxrsServer extends NettyJaxrsServer {

	/*private final int			ioWorkerCount		= Runtime.getRuntime().availableProcessors() * 2;
	private final int			executorThreadCount	= 16;
	private SSLContext			sslContext;
	private final int			maxRequestSize		= 1024 * 1024 * 10;
	static final ChannelGroup	allChannels			= new DefaultChannelGroup("NettyJaxrsServer");

	*//**
	 * expose bootstrap to be able to config
	 * 
	 * @return
	 *//*
	public Bootstrap initBootstrap() {
		this.bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool(),
						ioWorkerCount));
		return bootstrap;
	}

	public void setBootstrap(ServerBootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	@Override
	public void start()
	{
		deployment.start();
		RequestDispatcher dispatcher = new RequestDispatcher(
				(SynchronousDispatcher) deployment.getDispatcher(),
				deployment.getProviderFactory(), domain);
		
		// Configure the server.
		if (bootstrap == null) {
			initBootstrap();
		}

		ChannelPipelineFactory factory;
		if (sslContext == null) {
			factory = new HttpServerPipelineFactory(dispatcher, root, executorThreadCount, maxRequestSize);
		} else {
			factory = new HttpsServerPipelineFactory(dispatcher, root, executorThreadCount, maxRequestSize, sslContext);
		}
		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(factory);

		// Bind and start to accept incoming connections.
		channel = bootstrap.bind(new InetSocketAddress(port));
		allChannels.add(channel);
	}*/

	private EventLoopGroup eventLoopGroup;
	private EventLoopGroup eventExecutor;
	private int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
	private int executorThreadCount = 16;
	private SSLContext sslContext;
	private int maxRequestSize = 1024 * 1024 * 10;
	private int backlog = 128;

	@Override
	public void setSSLContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	/**
	 * Specify the worker count to use. For more information about this please see the javadocs of {@link io.netty.channel.EventLoopGroup}
	 *
	 * @param ioWorkerCount
	 */
	@Override
	public void setIoWorkerCount(int ioWorkerCount) {
		this.ioWorkerCount = ioWorkerCount;
	}

	/**
	 * Set the number of threads to use for the EventExecutor. For more information please see the javadocs of {@link io.netty.util.concurrent.EventExecutor}.
	 * If you want to disable the use of the {@link io.netty.util.concurrent.EventExecutor} specify a value <= 0.  This should only be done if you are 100% sure that you don't have any blocking
	 * code in there.
	 *
	 * @param executorThreadCount
	 */
	@Override
	public void setExecutorThreadCount(int executorThreadCount) {
		this.executorThreadCount = executorThreadCount;
	}

	/**
	 * Set the max. request size in bytes. If this size is exceed we will send a "413 Request Entity Too Large" to the client.
	 *
	 * @param maxRequestSize the max request size. This is 10mb by default.
	 */
	@Override
	public void setMaxRequestSize(int maxRequestSize) {
		this.maxRequestSize  = maxRequestSize;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	@Override
	public void start() {
		eventLoopGroup = new NioEventLoopGroup(ioWorkerCount);
		eventExecutor = new NioEventLoopGroup(executorThreadCount);
		deployment.start();
		final RequestDispatcher dispatcher = new RequestDispatcher((SynchronousDispatcher)deployment.getDispatcher(), deployment.getProviderFactory(), domain);
		// Configure the server.
		if (sslContext == null) {
			bootstrap.group(eventLoopGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new HttpRequestDecoder());
							ch.pipeline().addLast(new HttpObjectAggregator(maxRequestSize));
							ch.pipeline().addLast(new HttpResponseEncoder());
							ch.pipeline().addLast(new RestEasyHttpRequestDecoder(dispatcher.getDispatcher(), root, RestEasyHttpRequestDecoder.Protocol.HTTP));
							ch.pipeline().addLast(new CorsHeadersChannelHandler());
							ch.pipeline().addLast(new RestEasyHttpResponseEncoder());
							ch.pipeline().addLast(eventExecutor, new RequestHandler(dispatcher));
						}
					})
					.option(ChannelOption.SO_BACKLOG, backlog)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
		} else {
			final SSLEngine engine = sslContext.createSSLEngine();
			engine.setUseClientMode(false);
			bootstrap.group(eventLoopGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addFirst(new SslHandler(engine));
							ch.pipeline().addLast(new HttpRequestDecoder());
							ch.pipeline().addLast(new HttpObjectAggregator(maxRequestSize));
							ch.pipeline().addLast(new HttpResponseEncoder());
							ch.pipeline().addLast(new RestEasyHttpRequestDecoder(dispatcher.getDispatcher(), root, RestEasyHttpRequestDecoder.Protocol.HTTPS));
							ch.pipeline().addLast(new CorsHeadersChannelHandler());
							ch.pipeline().addLast(new RestEasyHttpResponseEncoder());
							ch.pipeline().addLast(eventExecutor, new RequestHandler(dispatcher));
						}
					})
					.option(ChannelOption.SO_BACKLOG, backlog)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
		}
		bootstrap.bind(port).syncUninterruptibly();
	}

}
