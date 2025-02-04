/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.net.netty.listener.impl;

import io.dingodb.common.concurrent.ThreadPoolBuilder;
import io.dingodb.net.netty.NetServiceConfiguration;
import io.dingodb.net.netty.connection.ConnectionManager;
import io.dingodb.net.netty.connection.ServerConnection;
import io.dingodb.net.netty.handler.ExceptionHandler;
import io.dingodb.net.netty.handler.MessageHandler;
import io.dingodb.net.netty.listener.PortListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Builder;

import static java.util.concurrent.TimeUnit.SECONDS;

@Builder
public class NettyServer implements PortListener {

    private final ConnectionManager connectionManager;
    private final int port;
    private EventLoopGroup eventLoopGroup;
    private ServerBootstrap server;

    @Override
    public int port() {
        return port;
    }

    @Override
    public void init() {
        server = new ServerBootstrap();
        eventLoopGroup = new NioEventLoopGroup(2, new ThreadPoolBuilder().name("Netty server " + port).build());
        server
            .localAddress(port)
            .channel(NioServerSocketChannel.class)
            .group(eventLoopGroup)
            .childHandler(channelInitializer());
    }

    @Override
    public void start() throws Exception {
        server.bind().sync().await();
    }

    private ChannelInitializer<SocketChannel> channelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ServerConnection connection = new ServerConnection(ch);
                ch.pipeline()
                    .addLast(new MessageHandler(connection))
                    .addLast(new IdleStateHandler(NetServiceConfiguration.INSTANCE.getHeartbeat(), 0, 0, SECONDS))
                    .addLast(new ExceptionHandler(connection));
                connectionManager.onOpen(connection);
                ch.closeFuture().addListener(future -> connectionManager.onClose(connection));
            }
        };
    }

    @Override
    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

}
