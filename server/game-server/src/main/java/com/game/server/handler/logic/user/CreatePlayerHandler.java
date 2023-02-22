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

/**
*
* @author KING
* @date 2023/1/31 11:15:39
*/
@Slf4j
@Service
@RequiredArgsConstructor
@HandlerTrigger(msgId = MsgId.EN_MessageId.E_CREATE_PLAYER_VALUE)
public class CreatePlayerHandler extends LogicMessageHandler {
    private final UserService userService;

    @Override
    public void execute(ChannelHandlerContext ctx, String playerId, byte[] bodyBytes, int reqSeq) throws Exception {
        LoginStructs.CreatePlayerReq req = null;
        try{
            req = LoginStructs.CreatePlayerReq.parseFrom(bodyBytes);
        }catch (Exception e) {
            log.error("CreatePlayerHandler CreatePlayerReq parse req have error.",e);
            throw new ReqParseException("CreatePlayerHandler CreatePlayerReq parse error.");
        }
        log.debug("receive E_CREATE_PLAYER CreatePlayerReq：\n{}", req);

        Common.Player player = userService.createPlayer(ctx,req.getPlayerName());

        LoginStructs.CreatePlayerResp.Builder respBuilder = LoginStructs.CreatePlayerResp.newBuilder();
        if (player != null){
            respBuilder.setPlayer(player);
        }
        log.debug("resp:\n{}",respBuilder.toString());

        super.sendNeedForwardMessage(ctx,playerId,MsgId.EN_MessageId.E_CREATE_PLAYER,respBuilder.build().toByteArray(),reqSeq, Code.En_Code.E_SUSS);
    }

}
