package com.game.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2023/02/16
 */
@Slf4j
@Data
@AllArgsConstructor
public class ConnectionIdInfo {
    private int serverType;
    private int serverId;
    private int connId;

}
