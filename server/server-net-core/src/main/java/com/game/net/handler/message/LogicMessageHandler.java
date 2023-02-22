package com.game.net.handler.message;

import com.game.net.message.InnerMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author KING
 * @date 2023/02/14
 */
public abstract class LogicMessageHandler extends BaseBusinessHandler {
    @Override
    public void execute(ChannelHandlerContext ctx, byte[] bodyBytes, int reqSeq) throws Exception {

    }

    @Override
    public void executeInnerMessage(ChannelHandlerContext ctx, InnerMessage innerMessage) {

    }
}
