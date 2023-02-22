package com.game.gateway.netty.outer;

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
public class OuterServerStarter implements StarterInterface {
    private final ForClientInitializer initializer;

    private final GatewayForClientDispatcher dispatcher;
    private final OuterTrigger trigger;

    private final GatewayForClientHandler socketHandler;
    private final GatewayForClientServer nettyServer;
    @Value("${netty.server.outer.port}")
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
        if(nettyServer == null){
            log.error("starter nettyServer is null");
            return false;
        }
         nettyServer.setInitializer(initializer);
        return true;
    }

    public boolean start(){
        if(!initAllComponent()){
            log.error("init component have error");
            return false;
        }
        nettyServer.start(port);
        log.info("outer start server suss!!!!!");
        return true;
    }
}
