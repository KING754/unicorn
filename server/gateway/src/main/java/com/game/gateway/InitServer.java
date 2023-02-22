package com.game.gateway;

import com.game.gateway.netty.inner.InnerServerStarter;
import com.game.gateway.netty.outer.OuterServerStarter;
import com.game.gateway.service.LogicConnectionManager;
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
    private final OuterServerStarter outerServerStarter;
    private final InnerServerStarter innerServerStarter;
    private final LogicConnectionManager connectionManager;


    @Override
    public void run(String... args) throws Exception {
        outerServerStarter.start();
        innerServerStarter.start();
        connectionManager.checkServerTypeCfg();
    }
}
