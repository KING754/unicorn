package com.game.server.handler.logic.user;

import com.game.exception.logic.ReqParseException;
import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.LogicMessageHandler;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
import com.game.rpc.message.st.user.LoginStructs;
import com.game.server.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
*
* @author KING
* @date 2023/1/31 11:15:39
*/
@Slf4j
@Service
@RequiredArgsConstructor
@HandlerTrigger(msgId = MsgId.EN_MessageId.E_CREATE_ACCOUNT_VALUE)
public class CreateAccountHandler extends LogicMessageHandler {
    private final UserService userService;

    @Override
    public void execute(ChannelHandlerContext ctx, String playerId, byte[] bodyBytes, int reqSeq) throws Exception {
        LoginStructs.CreateAccountReq req = null;
        try{
            req = LoginStructs.CreateAccountReq.parseFrom(bodyBytes);
        }catch (Exception e) {
            log.error("CreateAccountHandler CreateAccountReq parse req have error.",e);
            throw new ReqParseException("CreateAccountHandler CreateAccountReq parse error.");
        }

        userService.createAccount(ctx,req.getAccountName(),req.getPassword());

        log.info("receive E_CREATE_ACCOUNT CreateAccountReq：\n{}", req);

        super.sendNeedForwardMessage(ctx,playerId,MsgId.EN_MessageId.E_CREATE_ACCOUNT,null,reqSeq, Code.En_Code.E_SUSS);
    }

}
