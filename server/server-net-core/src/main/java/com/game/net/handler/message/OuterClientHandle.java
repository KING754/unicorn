package com.game.net.handler.message;

import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * @author KING
 * @date 2022/12/30
 */
public abstract class OuterClientHandle extends BaseServiceHandler {

    public abstract void clientExecute(ChannelHandlerContext ctx, short code, byte[] bodyBytes, int reqSeq);

    /**
     * 发送测试数据
     */
    public abstract void sendTestData(ChannelHandlerContext ctx) throws UnsupportedEncodingException;
}
