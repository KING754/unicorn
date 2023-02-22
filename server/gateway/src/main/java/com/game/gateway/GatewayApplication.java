package com.game.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.game.*")
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
/**
 * 0-1：gateway打开两个端口[OK]
 * 1.先创建 netty 客户端连接池[]
 * 2.连接gateway
 * 3.逻辑服处理
 *
 *
 *
 */

}
