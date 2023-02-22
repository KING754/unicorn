package com.game.gateway.netty.outer;

import com.game.net.handler.socket.SocketHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2023/02/13
 */
@Slf4j
@Data
@Component
public class GatewayForClientHandler extends SocketHandler {
}
