package com.game.server;

import com.game.server.netty.LogicServiceGatewayClientStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/01/09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitServer implements CommandLineRunner {
    private final LogicServiceGatewayClientStarter gatewayClientStarter;

    @Override
    public void run(String... args) throws Exception {
        gatewayClientStarter.start();
    }
}
