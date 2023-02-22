package com.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author KING
 * @date 2023/02/10
 */
@Data
@ToString
@AllArgsConstructor
public class ServerCfgPojo {
    private String ip;
    private int port;
}
