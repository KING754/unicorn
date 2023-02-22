package com.game.net.server;

import com.game.net.config.NettyProperties;
import com.game.net.server.initializer.BaseInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/09
 */
@Slf4j
@Data
@Component
public class NettyServer {
    private BaseInitializer initializer;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Class channelClass;
    private Channel serverChannel;


    public void start(int port) {
        this.initNetComponents();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(channelClass)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(initializer);
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                serverChannel = future.channel();
                log.info("server start suss,bind port: {},bossCount:{},workCount:{}", port,
                        NettyProperties.getBossThreadCount(), NettyProperties.getWorkThreadCount());
            } else {
                Throwable throwable = future.cause();
                log.info("server start suss,bind port: {},bossCount:{},workCount:{}.e:{}", port,
                        NettyProperties.getBossThreadCount(), NettyProperties.getWorkThreadCount(), throwable.getMessage());
            }
        } catch (InterruptedException e) {
            log.error("server start fail", e);
        }
    }

    private void initNetComponents() {
        if (NettyProperties.isLinux()) {
            this.bossGroup = new EpollEventLoopGroup(NettyProperties.getBossThreadCount());
            this.workGroup = new EpollEventLoopGroup(NettyProperties.getWorkThreadCount());
            this.channelClass = EpollServerSocketChannel.class;
        } else {
            this.bossGroup = new NioEventLoopGroup(NettyProperties.getBossThreadCount());
            this.workGroup = new NioEventLoopGroup(NettyProperties.getWorkThreadCount());
            this.channelClass = NioServerSocketChannel.class;
        }
    }

    private void closeHook(){
        if(bossGroup != null){
            bossGroup.shutdownGracefully();
        }
        if(workGroup != null){
            workGroup.shutdownGracefully();
        }
    }
}
