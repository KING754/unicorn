package com.game.client.netty;

import com.game.net.handler.trigger.SocketHandlerTrigger;
import com.game.net.message.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/10
 */
@Data
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientSocketHandlerTrigger implements SocketHandlerTrigger {
    private final TestClientDispatcher dispatcher;
    @Override
    public void doActive(ChannelHandlerContext ctx) {
        dispatcher.dispatch(ctx,null);
    }

    @Override
    public void doRead0(ChannelHandlerContext ctx, Message msg) {
        log.info("@@@@@@@@@@--->read--->{}",msg);
    }

    @Override
    public void doException(ChannelHandlerContext ctx, Throwable cause) {

    }

    @Override
    public void doInactive(ChannelHandlerContext ctx) {

    }
}

