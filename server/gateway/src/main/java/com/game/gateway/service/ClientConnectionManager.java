package com.game.gateway.service;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KING
 * @date 2023/02/15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientConnectionManager {

    private static final ConcurrentHashMap<String, ChannelHandlerContext> PLAYER_CONNECTION_MAP = new ConcurrentHashMap<>();

    public void addPlayerConnection(String playerId,ChannelHandlerContext ctx){
        PLAYER_CONNECTION_MAP.put(playerId,ctx);
    }

    public void removePlayerConnection(String playerId){
        PLAYER_CONNECTION_MAP.remove(playerId);
    }

    public ChannelHandlerContext getPlayerConnection(String playerId){
        return PLAYER_CONNECTION_MAP.get(playerId);
    }

}
