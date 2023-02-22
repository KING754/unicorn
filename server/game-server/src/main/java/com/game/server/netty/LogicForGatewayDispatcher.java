package com.game.server.netty;

import com.game.exception.LogicException;
import com.game.net.dispatcher.BaseDispatcher;
import com.game.net.handler.message.InnerMessageHandler;
import com.game.net.handler.message.manager.InnerMessageHandlerManager;
import com.game.net.message.InnerMessage;
import com.game.net.message.Message;
import com.game.net.message.MessageHead;
import com.game.rpc.message.inner.InnerRPC;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
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
public class LogicForGatewayDispatcher extends BaseDispatcher {
    private final InnerMessageHandlerManager handlerManager;

    public void dispatch(ChannelHandlerContext ctx, Message message) {
        if (!(message instanceof InnerMessage innerMessage)) {
            log.error("LogicForGateWayDispatcher receive message,but is not InnerMessage.msg:{}", message);
            return;
        }

        int innerMsgId = innerMessage.getServerMsgId();
        InnerRPC.EN_INNER_CMD_ID cmd = InnerRPC.EN_INNER_CMD_ID.forNumber(innerMsgId);
        if (cmd == null || cmd.getNumber() == InnerRPC.EN_INNER_CMD_ID.E_INNER_CMD_ERROR_ID_VALUE) {
            log.error("LogicForGateWayDispatcher receive message,but msgId is error.msg:{}", message);
            return;
        }

        boolean isOuterMessage = (cmd.getNumber() == InnerRPC.EN_INNER_CMD_ID.E_FORWARD_MSG_VALUE);

        MessageHead head = innerMessage.getHead();
        MsgId.EN_MessageId logicMessageId = null;
        int reqSequenceNum = 0;
        if (isOuterMessage) {
            if(head == null || head.getMsgId() <= 0){
                log.error("LogicForGateWayDispatcher receive forward message,but message head is null.msg:{}", message);
                return;
            }else{
                logicMessageId = MsgId.EN_MessageId.forNumber(head.getMsgId());
                reqSequenceNum = head.getSeq();
            }
        }

        InnerMessageHandler handler = handlerManager.getHandlerByMsgId((short) cmd.getNumber());
        if (handler != null) {
            try {
                handler.executeInnerMessage(ctx,innerMessage);
            } catch (Exception e) {
                if (e instanceof LogicException logicException) {
                    log.error("handler have error.msg:",logicException);
                    if(isOuterMessage){
                        super.sendNeedForwardErrorMessage(ctx, innerMessage.getPlayerId(),logicMessageId,reqSequenceNum,logicException.getCode());
                    }
                    /**
                     * 防止消息风暴,做为gateway的client一侧.如果内部消息,执行中遇到错误,则不在回复消息,所以没有else
                     */
                } else {
                    log.error("handler have exception msg:{}", message, e);
                    if(isOuterMessage){
                        super.sendNeedForwardErrorMessage(ctx, innerMessage.getPlayerId(),logicMessageId,reqSequenceNum, Code.En_Code.E_UNKNOWN_ERROR);
                    }
                    /**
                     * 防止消息风暴,做为gateway的client一侧.如果内部消息,执行中遇到错误,则不在回复消息,所以没有else
                     */
                }
            }
        } else {
            log.error("LogicForGateWayDispatcher dispatch fail.server not handle for:{}", cmd);
            if(isOuterMessage){
                super.sendNeedForwardErrorMessage(ctx, innerMessage.getPlayerId(),logicMessageId,reqSequenceNum, Code.En_Code.E_SYSTEM_ERROR);
            }
            /**
             * 防止消息风暴,做为gateway的client一侧.如果内部消息,执行中遇到错误,则不在回复消息,所以没有else
             */
        }
    }

}
