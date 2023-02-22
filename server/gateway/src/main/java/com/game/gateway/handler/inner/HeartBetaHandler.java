package com.game.gateway.handler.inner;

import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.InnerMessageHandler;
import com.game.net.message.InnerMessage;
import com.game.rpc.message.inner.InnerRPC;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/14
 */
@Slf4j
@Component
@HandlerTrigger(msgId = InnerRPC.EN_INNER_CMD_ID.E_HEART_BETA_VALUE)
public class HeartBetaHandler extends InnerMessageHandler {

    @Override
    public void executeInnerMessage(ChannelHandlerContext ctx, InnerMessage innerMessage) {
        log.info("receive heart beta req remote address：{}",ctx.channel().remoteAddress());
        super.sendInnerMessage(ctx, InnerRPC.EN_INNER_CMD_ID.E_REGIST_SERVICE, InnerRPC.EN_INNER_CODE.E_INNER_SUSS,null);
    }
}
