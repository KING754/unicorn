package com.game.server.cache;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KING
 * @date 2023/01/03
 */
@Service
public class ClientChannelManager {
    private static ConcurrentHashMap<String, ChannelHandlerContext> ACCOUNT_ID_CHANNEL = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ChannelHandlerContext> PLAYER_ID_CHANNEL = new ConcurrentHashMap<>();


    public void addAccountConnection(String accountId, ChannelHandlerContext ctx) {
        ACCOUNT_ID_CHANNEL.put(accountId, ctx);
    }

    public void addPlayerConnection(String playerId, ChannelHandlerContext ctx) {
        PLAYER_ID_CHANNEL.put(playerId, ctx);
    }

    public String getAccountId(ChannelHandlerContext ctx) {
        for (Entry<String, ChannelHandlerContext> entry : ACCOUNT_ID_CHANNEL.entrySet()) {
            if(entry.getValue().equals(ctx)){
                return entry.getKey();
            }
        }
        return StrUtil.EMPTY;
    }

    public String getPlayerId(ChannelHandlerContext ctx) {
        for (Entry<String, ChannelHandlerContext> entry : PLAYER_ID_CHANNEL.entrySet()) {
            if(entry.getValue().equals(ctx)){
                return entry.getKey();
            }
        }
        return StrUtil.EMPTY;
    }

}
