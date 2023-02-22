package com.game.net.handler.socket;

import com.game.net.dispatcher.DispatcherInterface;
import com.game.net.handler.trigger.SocketHandlerTrigger;
import com.game.net.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2022/12/28
 */
@Data
@Slf4j
@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class SocketHandler extends SimpleChannelInboundHandler<Message> {
    private DispatcherInterface dispatch;
    private SocketHandlerTrigger trigger;

    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("server receive connection: {}",  ctx.channel().remoteAddress());
        if(trigger != null){
            trigger.doActive(ctx);
        }
        super.channelActive(ctx);
    }

    /**
     * 客户端发消息会触发
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        log.info("server receive msg: {}", msg.toString());
        if(trigger != null){
            trigger.doRead0(ctx,msg);
        }
        dispatch.dispatch(ctx, msg);
    }


    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("connection exception is close.IP:{},e:{}", ctx.channel().remoteAddress(),cause);
        if(trigger != null){
            trigger.doException(ctx,cause);
        }
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("connection is close.IP:{}", ctx.channel().remoteAddress());
        if(trigger != null){
            trigger.doInactive(ctx);
        }
        super.channelInactive(ctx);
    }


}
