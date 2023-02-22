package com.game.net.server;

import com.game.net.server.initializer.BaseInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2022/12/27
 */

@Slf4j
@Data
@Component
public class NettyClient {
    private BaseInitializer initializer;
    private  EventLoopGroup workerGroup;
    public void start(String ip,int port) {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(initializer);
            ChannelFuture f = b.connect(ip, port).sync();
            log.info("connection server：{}：{} suss",ip, port);
        } catch (InterruptedException e) {
            e.printStackTrace();
//            this.closeHook();
        }
    }

    private void closeHook(){
        if(workerGroup != null){
            workerGroup.shutdownGracefully();
        }
    }
}
