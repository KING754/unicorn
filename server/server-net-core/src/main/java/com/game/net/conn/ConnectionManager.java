package com.game.net.conn;

import com.game.constant.ServerConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import com.game.vo.ConnectionIdInfo;

/**
 * @author KING
 * @date 2023/02/16
 */
public class ConnectionManager {

    public void setStrAttrForNettyCtx(ChannelHandlerContext ctx, String key, String value) {
        AttributeKey<String> typeAttr = AttributeKey.valueOf(key);
        ctx.attr(typeAttr).set(value);
    }

    public void setIntAttrForNettyCtx(ChannelHandlerContext ctx, String key, Integer value) {
        AttributeKey<Integer> typeAttr = AttributeKey.valueOf(key);
        ctx.attr(typeAttr).set(value);
    }


    public String getStrAttrForNettyCtx(ChannelHandlerContext ctx, String key) {
        AttributeKey<String> typeAttr = AttributeKey.valueOf(key);
        return ctx.attr(typeAttr).get();
    }

    public Integer getIntAttrForNettyCtx(ChannelHandlerContext ctx, String key) {
        AttributeKey<Integer> typeAttr = AttributeKey.valueOf(key);
        return ctx.attr(typeAttr).get();
    }

    public ConnectionIdInfo getLogicServiceNettyCtxIdInfo(ChannelHandlerContext ctx){
        if(ctx == null){
            return null;
        }

        Integer serverType = this.getIntAttrForNettyCtx(ctx,ServerConstant.NETTY_CTX_SERVER_TYPE_ATTR_KEY);
        Integer serverId = this.getIntAttrForNettyCtx(ctx,ServerConstant.NETTY_CTX_SERVER_ID_ATTR_KEY);
        Integer connId = this.getIntAttrForNettyCtx(ctx,ServerConstant.NETTY_CTX_CONNECTION_ID_ATTR_KEY);

        return new ConnectionIdInfo(serverType,serverId,connId);
    }




}
