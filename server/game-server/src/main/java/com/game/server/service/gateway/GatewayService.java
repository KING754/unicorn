package com.game.server.service.gateway;

import com.game.exception.logic.ServiceException;
import com.game.net.config.ServerConfig;
import com.game.net.handler.message.LogicMessageHandler;
import com.game.net.handler.message.manager.LogicHandlerManager;
import com.game.net.message.InnerMessage;
import com.game.net.message.MessageHead;
import com.game.rpc.message.inner.InnerRPC;
import com.game.server.service.connection.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayService {
    private final ServerConfig serverConfig;
    private final ConnectionManager connectionManager;
    private final LogicHandlerManager logicHandlerManager;

    public InnerRPC.RegisterServiceReq buildRegisterReq(){
        InnerRPC.RegisterServiceReq.Builder reqBuilder = InnerRPC.RegisterServiceReq.newBuilder();
        reqBuilder.setServerId(serverConfig.getId());
        reqBuilder.setConnId(connectionManager.getConnectionId());
        reqBuilder.setServerType(InnerRPC.EN_Server_type.forNumber(serverConfig.getType()));
        reqBuilder.setStartMsgId(serverConfig.getReceiveMinMsg());
        reqBuilder.setEndMsgId(serverConfig.getReceiveMaxMsg());

        return reqBuilder.build();
    }


    public void forwardMessage(ChannelHandlerContext ctx,InnerMessage innerMessage) throws Exception {
        MessageHead head = innerMessage.getHead();
        short logicMessageId = head.getMsgId();
        int reqNo = head.getSeq();



        LogicMessageHandler logicHandler = logicHandlerManager.getHandlerByMsgId(logicMessageId);
        if(logicHandler == null){
            throw new ServiceException("GatewayService not found msgId:"+logicMessageId+" logicHandler");
        }

        logicHandler.execute(ctx, innerMessage.getPlayerId(), innerMessage.getBody(),head.getSeq());
    }
}
