package com.game.server.netty;

import com.game.net.handler.trigger.SocketHandlerTrigger;
import com.game.net.message.Message;
import com.game.server.handler.gateway.RegisterServiceHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/13
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class InnerTrigger implements SocketHandlerTrigger {
    private final RegisterServiceHandler registerServiceHandler;
    @Override
    public void doActive(ChannelHandlerContext ctx) {
        log.info("InnerTrigger doActive !!!!");
        registerServiceHandler.reqRegister(ctx);
    }

    @Override
    public void doRead0(ChannelHandlerContext ctx, Message msg) {

    }

    @Override
    public void doException(ChannelHandlerContext ctx, Throwable cause) {

    }

    @Override
    public void doInactive(ChannelHandlerContext ctx) {

    }
}
