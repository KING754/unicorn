package com.game.gateway.handler.inner;

import com.game.exception.logic.ReqParseException;
import com.game.gateway.service.LogicMessageService;
import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.InnerMessageHandler;
import com.game.net.message.InnerMessage;
import com.game.rpc.message.inner.InnerRPC;
import com.google.protobuf.InvalidProtocolBufferException;
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
    private final LogicMessageService logicServiceManager;


    @Override
    public void executeInnerMessage(ChannelHandlerContext ctx, InnerMessage innerMessage) {
        if(innerMessage.getBody() == null || innerMessage.getBody().length == 0){
            throw  new ReqParseException("RegisterServiceHandler innerMessage is null or empty");
        }

        InnerRPC.RegisterServiceReq registerServiceReq = null;
        try {
            registerServiceReq = InnerRPC.RegisterServiceReq.parseFrom(innerMessage.getBody());
        } catch (InvalidProtocolBufferException e) {
            log.error("RegisterServiceHandler parse registerServiceReq have error.",e);
        }

        if(registerServiceReq == null){
            throw new ReqParseException("RegisterServiceHandler innerMessage body after parse is null");
        }


        logicServiceManager.registerLogicService(registerServiceReq,ctx);

        super.sendInnerMessage(ctx, InnerRPC.EN_INNER_CMD_ID.E_REGIST_SERVICE, InnerRPC.EN_INNER_CODE.E_INNER_SUSS,null);
    }
}
