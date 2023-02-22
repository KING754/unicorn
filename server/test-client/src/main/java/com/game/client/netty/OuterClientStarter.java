package com.game.client.netty;

import com.game.net.handler.socket.SocketHandler;
import com.game.net.server.NettyClient;
import com.game.net.start.StarterInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author KING
 * @date 2022/12/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OuterClientStarter implements StarterInterface {
    private final ForClientInitializer initializer;
    private final TestClientDispatcher dispatcher;
    private final ClientSocketHandlerTrigger trigger;

    private final SocketHandler socketHandler;
    private final NettyClient nettyClient;
    @Value("${netty.server.ip}")
    private String ip;
    @Value("${netty.server.port}")
    private int port;



    @Override
    public boolean initDispatcher() {
        if(dispatcher == null){
            log.error("starter dispatcher is null");
            return false;
        }
        return true;
    }

    @Override
    public boolean initTrigger() {
        if(trigger == null){
            log.error("starter trigger is null");
            return false;
        }
        return true;
    }

    @Override
    public boolean initSocketHandler() {
        if(socketHandler == null){
            log.error("starter socketHandler is null");
            return false;
        }
        socketHandler.setDispatch(dispatcher);
        if(trigger != null){
            socketHandler.setTrigger(trigger);
        }
        return true;
    }

    @Override
    public boolean initInitializer() {
        if(initializer == null){
            log.error("starter initializer is null");
            return false;
        }
        initializer.setHandler(socketHandler);
        return true;
    }

    @Override
    public boolean initServerOrClient() {
        if(nettyClient == null){
            log.error("starter nettyClient is null");
            return false;
        }
        nettyClient.setInitializer(initializer);
        return true;
    }

    public boolean start(){
        if(!initAllComponent()){
            log.error("init component have error");
            return false;
        }
        nettyClient.start(ip,port);
        log.info("clint connection {}:{} suss",ip,port);
        return true;
    }
}
