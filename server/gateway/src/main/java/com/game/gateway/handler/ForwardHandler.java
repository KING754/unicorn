package com.game.gateway.handler;

import com.game.gateway.service.ForwardService;
import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.InnerMessageHandler;
import com.game.net.message.InnerMessage;
import com.game.net.message.Message;
import com.game.rpc.message.inner.InnerRPC;
import com.game.rpc.message.st.MsgId;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/16
 */

@Slf4j
@Data
@Component
@RequiredArgsConstructor
@HandlerTrigger(msgId = InnerRPC.EN_INNER_CMD_ID.E_FORWARD_MSG_VALUE)
public class ForwardHandler extends InnerMessageHandler {
    private final ForwardService forwardService;

    public void execute(ChannelHandlerContext clientCtx, Message message, MsgId.EN_MessageId msgIdEnum){
        forwardService.forwardInnerMessage(clientCtx,message,msgIdEnum);
    }

    @Override
    public void executeInnerMessage(ChannelHandlerContext logicServiceCtx, InnerMessage innerMessage) {
        forwardService.forwardInnerMessage(innerMessage);
    }
}
