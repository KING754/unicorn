package org.unicorn.common.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 业务处理类
 * 
 * @author KING
 * @date 2018年8月21日
 */

public class ServerIoHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerIoHandler.class);

    /**
     * 一个连接建立成功时的回调方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 收到消息的回调方法
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("server receive:"+msg);
        ctx.writeAndFlush("已收到消息");
    }
    
    /**连接发生异常时(如关闭)的回调方法*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

}
