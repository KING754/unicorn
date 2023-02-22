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
@HandlerTrigger(msgId = InnerRPC.EN_INNER_CMD_ID.E_REGIST_SERVICE_VALUE)
public class RegisterServiceHandler extends InnerMessageHandler {
    private final GatewayService gatewayService;

    @Override
    public void executeInnerMessage(ChannelHandlerContext ctx, InnerMessage innerMessage) {
        log.info("game server receive gateway resp:{}",innerMessage);
        if(innerMessage.getCode() == InnerRPC.EN_INNER_CODE.E_INNER_SUSS_VALUE){
            log.info("register suss !!!");
        }
    }
    public void reqRegister(ChannelHandlerContext ctx){
        InnerRPC.RegisterServiceReq registerReq = gatewayService.buildRegisterReq();
        super.sendInnerMessage(ctx, InnerRPC.EN_INNER_CMD_ID.E_REGIST_SERVICE, InnerRPC.EN_INNER_CODE.E_INNER_SUSS,registerReq.toByteArray());
    }


}
