package com.game.gateway.netty.outer;

import com.game.net.handler.trigger.SocketHandlerTrigger;
import com.game.net.message.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/13
 */

@Slf4j
@Component
public class OuterTrigger implements SocketHandlerTrigger {
    @Override
    public void doActive(ChannelHandlerContext ctx) {
        log.info("OuterTrigger doActive !!!!");
    }

    @Override
    public void doRead0(ChannelHandlerContext ctx, Message msg) {
        log.info("OuterTrigger doRead0 !!!!");
    }

    @Override
    public void doException(ChannelHandlerContext ctx, Throwable cause) {

    }

    @Override
    public void doInactive(ChannelHandlerContext ctx) {

    }
}
