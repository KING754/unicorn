package com.game.gateway.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.game.constant.ServerConstant;
import com.game.exception.logic.ServiceException;
import com.game.exception.logic.UserConnectionNotFoundException;
import com.game.factory.IdFactory;
import com.game.net.handler.message.BaseServiceHandler;
import com.game.net.message.InnerMessage;
import com.game.net.message.Message;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
import com.game.vo.ConnectionIdInfo;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/16
 */
@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class ForwardService extends BaseServiceHandler {
    private final LogicConnectionManager logicServiceConnectionManager;
    private final ClientConnectionManager clientConnectionManager;
    private final IdFactory idFactory;

    public void forwardInnerMessage(ChannelHandlerContext clientCtx, Message message, MsgId.EN_MessageId msgIdEnum){
        Integer serverType = logicServiceConnectionManager.getServerTypeByMsgId(msgIdEnum.getNumber());
        if(serverType == null || serverType <= 0){
            throw new ServiceException("not found deal msgId:"+msgIdEnum.getNumber()+" logic service.");
        }

        String playerId = logicServiceConnectionManager.getStrAttrForNettyCtx(clientCtx,ServerConstant.PLAYER_NETTY_CTX_PLAYER_ID_KEY);
        if(StrUtil.isEmpty(playerId)){
            playerId = idFactory.getId();
            logicServiceConnectionManager.setStrAttrForNettyCtx(clientCtx,ServerConstant.PLAYER_NETTY_CTX_PLAYER_ID_KEY,playerId);
            clientConnectionManager.addPlayerConnection(playerId,clientCtx);
        }

        ChannelHandlerContext dealMessageLogicServiceCtx = null;

        String playerLogicServiceAttrKey = ServerConstant.getPlayerServerTypeConnAttrKey(serverType);
        String connectionIdStr = logicServiceConnectionManager.getStrAttrForNettyCtx(clientCtx,playerLogicServiceAttrKey);
        boolean isNewConn = false;

        if(StrUtil.isEmpty(connectionIdStr)){
            dealMessageLogicServiceCtx = logicServiceConnectionManager.getMinLoadLogicConn(serverType);
            isNewConn = true;
        }else{
            ConnectionIdInfo info = JSON.parseObject(connectionIdStr,ConnectionIdInfo.class);
            if(info != null){
                dealMessageLogicServiceCtx = logicServiceConnectionManager.getLogicConnByInfo(info);
            }
            if(dealMessageLogicServiceCtx == null){
                log.warn("the player old conn not found.playerId:{},old server:{}",playerId, JSON.toJSON(info));
                dealMessageLogicServiceCtx = logicServiceConnectionManager.getMinLoadLogicConn(serverType);
                isNewConn = true;
            }
        }

        if(dealMessageLogicServiceCtx == null){
            throw new ServiceException("not found the msgId:"+msgIdEnum.getNumber()+",dealMsgType:"+serverType);
        }else{
            if(isNewConn){
                ConnectionIdInfo newConnIdInfo = logicServiceConnectionManager.getLogicServiceNettyCtxIdInfo(dealMessageLogicServiceCtx);
                logicServiceConnectionManager.setStrAttrForNettyCtx(clientCtx,playerLogicServiceAttrKey,JSON.toJSONString(newConnIdInfo));
            }
        }

        super.sendNeedForwardMessage(dealMessageLogicServiceCtx,playerId,message);
    }


    public void forwardInnerMessage(InnerMessage innerMessage){
        String playerId = innerMessage.getPlayerId();
        ChannelHandlerContext clientCtx = clientConnectionManager.getPlayerConnection(playerId);
        if(clientCtx == null){          //这个地方可以关闭客户端进行测试
            UserConnectionNotFoundException exception = new UserConnectionNotFoundException("playerId:"+playerId+" not found the client connection");
            exception.setCode(Code.En_Code.E_SYSTEM_ERROR);
            throw exception;
        }
        Message message = new Message();
        message.setHead(innerMessage.getHead());
        message.setBody(innerMessage.getBody());
        super.sendMessage(clientCtx,message);
    }
}
