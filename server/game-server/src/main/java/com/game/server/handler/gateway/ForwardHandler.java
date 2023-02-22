package com.game.server.handler.gateway;

import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.InnerMessageHandler;
import com.game.net.message.InnerMessage;
import com.game.rpc.message.inner.InnerRPC;
import com.game.server.service.gateway.GatewayService;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/14
 */
@Slf4j
@Component
@RequiredArgsConstructor
@HandlerTrigger(msgId = InnerRPC.EN_INNER_CMD_ID.E_FORWARD_MSG_VALUE)
public class ForwardHandler extends InnerMessageHandler {
    private final GatewayService gatewayService;

    @Override
    public void executeInnerMessage(ChannelHandlerContext ctx, InnerMessage innerMessage) throws Exception {
        log.info("game server receive gateway resp:{}",innerMessage);
        gatewayService.forwardMessage(ctx,innerMessage);
    }

}
