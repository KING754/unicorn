package com.game.net.server.initializer;

import com.game.net.coder.InnerMessageDecoder;
import com.game.net.coder.InnerMessageEncoder;
import com.game.net.config.NettyProperties;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/13
 */
@Slf4j
@Data
@Component
public class InnerInitializer extends BaseInitializer {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        socketChannel.pipeline().addLast("decoder", new InnerMessageDecoder());
        socketChannel.pipeline().addLast("encoder", new InnerMessageEncoder());
        socketChannel.pipeline().addLast(new DefaultEventExecutorGroup(NettyProperties.getWorkThreadCount()),super.getHandler());
    }
}
