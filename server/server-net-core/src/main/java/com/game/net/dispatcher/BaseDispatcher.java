package com.game.net.dispatcher;

import com.game.net.handler.message.BaseServiceHandler;
import com.game.net.message.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @author KING
 * @date 2022/12/30
 */
@Data
@Slf4j
public class BaseDispatcher extends BaseServiceHandler implements DispatcherInterface{

    public void dispatch(ChannelHandlerContext ctx, Message message){

    }
}
