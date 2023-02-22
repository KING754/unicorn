package com.game.net.handler.trigger;

import com.game.net.message.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author KING
 * @date 2023/02/10
 */
public interface SocketHandlerTrigger {
    void doActive(ChannelHandlerContext ctx);
    void doRead0(ChannelHandlerContext ctx, Message msg);
    void doException(ChannelHandlerContext ctx, Throwable cause);
    void doInactive(ChannelHandlerContext ctx);
}
