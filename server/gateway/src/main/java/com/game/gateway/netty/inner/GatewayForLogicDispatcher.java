package com.game.gateway.netty.inner;

import com.game.exception.LogicException;
import com.game.net.dispatcher.BaseDispatcher;
import com.game.net.handler.message.InnerMessageHandler;
import com.game.net.handler.message.manager.InnerMessageHandlerManager;
import com.game.net.message.InnerMessage;
import com.game.net.message.Message;
import com.game.net.message.MessageHead;
import com.game.rpc.message.inner.InnerRPC;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author KING
 * @date 2022/12/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayForLogicDispatcher extends BaseDispatcher {
    private final InnerMessageHandlerManager handlerManager;

    public void dispatch(ChannelHandlerContext ctx, Message message){
        if(message == null){
            log.error("GatewayForLogicDispatcher receive null message");
            return;
        }

        if(!(message instanceof InnerMessage)){
            log.error("GatewayForLogicDispatcher receive message,but is not InnerMessage.msg:{}",message);
            return;
        }

        InnerMessage innerMessage = (InnerMessage) message;
        int innerMsgId = innerMessage.getServerMsgId();
        InnerRPC.EN_INNER_CMD_ID cmd = InnerRPC.EN_INNER_CMD_ID.forNumber(innerMsgId);
        if(cmd == null || cmd.getNumber() == InnerRPC.EN_INNER_CMD_ID.E_INNER_CMD_ERROR_ID_VALUE){
            log.error("GatewayForLogicDispatcher receive message,but cmd is error.msg:{}",message);
            return;
        }

        MessageHead head = innerMessage.getHead();
        if(cmd.getNumber() == InnerRPC.EN_INNER_CMD_ID.E_FORWARD_MSG_VALUE && (head == null || head.getMsgId() <= 0)){
            log.error("InnerDispatcher receive forward message,but message head is null.msg:{}",message);
            return;
        }

        int req = 0;
        if(head != null){
            req = head.getSeq();
        }

        InnerMessageHandler handler =handlerManager.getHandlerByMsgId((short)cmd.getNumber());
        if (handler != null) {
            try {
                handler.executeInnerMessage(ctx,innerMessage);
            } catch (Exception e) {
                if(e instanceof LogicException){
                    LogicException logicException = (LogicException)e;
                    log.warn("have logic error.exception:",logicException);
                    super.sendInnerErrorMessage(ctx,cmd,logicException.getInnerCode());
                }else{
                    log.error("handler have exception msg:{}",message,e);
                    super.sendInnerErrorMessage(ctx,cmd,InnerRPC.EN_INNER_CODE.E_INNER_UNKNOWN_ERROR);
                }
            }
        } else{
            log.error("dispatch fail.server not handle for:{}", cmd);
            super.sendInnerErrorMessage(ctx,cmd,InnerRPC.EN_INNER_CODE.E_INNER_SYSTEM_ERROR);
        }
    }

}
