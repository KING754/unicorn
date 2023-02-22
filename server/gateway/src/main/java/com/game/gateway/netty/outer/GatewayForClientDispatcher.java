package com.game.gateway.netty.outer;

import com.game.exception.LogicException;
import com.game.gateway.handler.ForwardHandler;
import com.game.net.dispatcher.BaseDispatcher;
import com.game.net.message.Message;
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
public class GatewayForClientDispatcher extends BaseDispatcher {
    private final ForwardHandler forwardHandler;

    public void dispatch(ChannelHandlerContext ctx, Message message){
        if(message == null || message.getHead() == null){
            log.error("client req message is null");
            super.sendErrorMessage(ctx, MsgId.EN_MessageId.NOTIFY_COMMON_ERROR,message.getHead().getSeq(), Code.En_Code.E_REQ_PARSE_ERROR);
            return;
        }

        short msgId = message.getHead().getMsgId();
        int msgReq = message.getHead().getSeq();
        MsgId.EN_MessageId msgIdEnum = MsgId.EN_MessageId.forNumber(msgId);
        if(msgIdEnum == null || msgIdEnum.getNumber() == MsgId.EN_MessageId.E_ERROR_ID_VALUE){
            log.error("client req msg Id is error.msg:{}",message);
            super.sendErrorMessage(ctx, MsgId.EN_MessageId.NOTIFY_COMMON_ERROR,message.getHead().getSeq(), Code.En_Code.E_REQ_PARSE_ERROR);
            return;
        }

        if (forwardHandler != null) {
            try {
                forwardHandler.execute(ctx, message,msgIdEnum);
            } catch (Exception e) {
                if(e instanceof LogicException){
                    LogicException logicException = (LogicException)e;
                    log.error("client dispatcher have error.error:{}",logicException);
                    super.sendErrorMessage(ctx,msgIdEnum,msgReq,logicException.getCode());
                }else{
                    log.error("client dispatcher handler have exception msg:{}",message,e);
                    super.sendErrorMessage(ctx,msgIdEnum,msgReq, Code.En_Code.E_UNKNOWN_ERROR);
                }
            }
        } else{
            log.error("dispatch fail.server not handle for:{}", msgId);
            super.sendErrorMessage(ctx,msgIdEnum,msgReq, Code.En_Code.E_SERVICE_ERROR);
        }
    }
}
