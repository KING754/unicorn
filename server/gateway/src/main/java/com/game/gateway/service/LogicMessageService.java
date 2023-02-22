package com.game.gateway.service;

import com.game.constant.ServerConstant;
import com.game.exception.inner.ConnectDuplicateException;
import com.game.rpc.message.inner.InnerRPC;
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
public class LogicMessageService {
    private final LogicConnectionManager connectionManager;

    public void registerLogicService(InnerRPC.RegisterServiceReq req, ChannelHandlerContext ctx){
        if(connectionManager.isHaveDealSameMsgIdOtherServerType(req.getServerType())){
            throw new ConnectDuplicateException("same deal msgId server.INFO:"+req.toString());
        }

        int serverType = req.getServerTypeValue();
        int serverId = req.getServerId();
        int connId = req.getConnId();
//        if(connectionManager.isHaveTheConnId(serverType,serverId,connId)){
//            throw new ConnectDuplicateException("have same conn: "+serverType+"_"+serverId+"_"+connId);
//        }

        connectionManager.setIntAttrForNettyCtx(ctx,ServerConstant.NETTY_CTX_SERVER_TYPE_ATTR_KEY,serverType);
        connectionManager.setIntAttrForNettyCtx(ctx,ServerConstant.NETTY_CTX_SERVER_ID_ATTR_KEY,serverId);
        connectionManager.setIntAttrForNettyCtx(ctx,ServerConstant.NETTY_CTX_CONNECTION_ID_ATTR_KEY,connId);

        connectionManager.putConnection(serverType,serverId,connId,ctx);
        connectionManager.putLogicService(req.getServerType());
    }
}
