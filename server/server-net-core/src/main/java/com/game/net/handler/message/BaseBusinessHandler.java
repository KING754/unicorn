package com.game.net.handler.message;

import com.game.net.message.InnerMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2022/12/30
 */
@Slf4j
public abstract class BaseBusinessHandler extends BaseServiceHandler {
    /**
     * 具体业务类需要实现该方法
     */
    public abstract void execute(ChannelHandlerContext ctx, String playerId, byte[] bodyBytes, int reqSeq) throws Exception ;

    public abstract void execute(ChannelHandlerContext ctx, byte[] bodyBytes, int reqSeq) throws Exception;


    public abstract void executeInnerMessage(ChannelHandlerContext ctx, InnerMessage innerMessage) throws Exception;



}
