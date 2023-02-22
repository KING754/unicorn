package com.game.client;

import com.game.client.netty.OuterClientStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2022/12/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientMain  implements CommandLineRunner {
    private final OuterClientStarter outerClientStarter;


    @Override
    public void run(String... args) throws Exception {
        outerClientStarter.start();
    }
}
