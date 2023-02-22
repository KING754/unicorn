package com.game.net.dispatcher;

import com.game.net.message.Message;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author KING
 * @date 2022/12/30
 */
public interface DispatcherInterface {
    public void dispatch(ChannelHandlerContext ctx, Message message);


}
