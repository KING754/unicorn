package com.game.net.server.initializer;

import com.game.net.coder.ClientMessageDecoder;
import com.game.net.coder.ClientMessageEncoder;
import com.game.net.config.NettyProperties;
import com.game.net.handler.socket.SocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2022/12/28
 */
@Data
@Slf4j
@Component
public class BaseInitializer extends ChannelInitializer<SocketChannel> {
    private SocketHandler handler;
    private ClientMessageDecoder deCoder;
    private ClientMessageEncoder enCoder;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        socketChannel.pipeline().addLast("decoder", new ClientMessageDecoder());
        socketChannel.pipeline().addLast("encoder", new ClientMessageEncoder());
        socketChannel.pipeline().addLast(new DefaultEventExecutorGroup(NettyProperties.getWorkThreadCount()),handler);
    }


}