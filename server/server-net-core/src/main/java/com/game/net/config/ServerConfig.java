package com.game.net.config;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/09
 */
@Data
@ToString
@Slf4j
@Component
@ConfigurationProperties(prefix = "netty.config")
public class ServerConfig {
    private int id;
    private int type;
    private int receiveMinMsg;
    private int receiveMaxMsg;
}
