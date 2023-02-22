package com.game.server.handler.logic.user;

import com.game.exception.logic.ReqParseException;
import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.LogicMessageHandler;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
import com.game.rpc.message.st.common.Common;
import com.game.rpc.message.st.user.LoginStructs;
import com.game.server.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*
* @author KING
* @date 2023/1/31 11:15:39
*/
@Slf4j
@Service
@RequiredArgsConstructor
@HandlerTrigger(msgId = MsgId.EN_MessageId.E_LOGIN_VALUE)
public class LoginHandler extends LogicMessageHandler {
    private final UserService userService;

    @Override
    public void execute(ChannelHandlerContext ctx, String playerId, byte[] bodyBytes, int reqSeq) throws Exception {
        LoginStructs.LoginReq req = null;
        try{
            req = LoginStructs.LoginReq.parseFrom(bodyBytes);
        }catch (Exception e) {
            log.error("LoginHandler LoginReq parse req have error.",e);
            throw new ReqParseException("LoginHandler LoginReq parse error.");
        }
        log.debug("receive E_LOGIN LoginReq：\n{}", req);

        List<Common.Player> playerList = userService.login(ctx,req.getUserName(),req.getPassWord());

        LoginStructs.LoginResp.Builder respBuilder = LoginStructs.LoginResp.newBuilder();
        if(playerList != null && !playerList.isEmpty()){
            respBuilder.setPlayer(playerList.get(0));
        }

        log.debug("resp:\n{}",respBuilder.toString());

        super.sendNeedForwardMessage(ctx,playerId,MsgId.EN_MessageId.E_LOGIN,respBuilder.build().toByteArray(),reqSeq, Code.En_Code.E_SUSS);
    }

}
