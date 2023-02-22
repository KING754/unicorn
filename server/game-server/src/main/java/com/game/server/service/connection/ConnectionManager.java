package com.game.server.service.connection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KING
 * @date 2023/02/15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectionManager {
    public static final AtomicInteger atomicInteger = new AtomicInteger(1);


    public int getConnectionId(){
        return atomicInteger.getAndIncrement();
    }
}
