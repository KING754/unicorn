package com.game.server.netty;

import cn.hutool.core.util.StrUtil;
import com.game.constant.StringConstant;
import com.game.net.config.ServerConfig;
import com.game.net.handler.socket.SocketHandler;
import com.game.net.server.NettyClient;
import com.game.net.server.initializer.InnerInitializer;
import com.game.net.start.StarterInterface;
import com.game.config.ServerCfgPojo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KING
 * @date 2022/12/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogicServiceGatewayClientStarter implements StarterInterface {
    private final InnerInitializer initializer;
    private final LogicForGatewayDispatcher dispatcher;
    private final InnerTrigger trigger;

    private final SocketHandler socketHandler;
    private final NettyClient nettyClient;

    private final ServerConfig serverConfig;

    @Value("${netty.gateways.address}")
    private String gateways;


    @Override
    public boolean initDispatcher() {
        if (dispatcher == null) {
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
        if (socketHandler == null) {
            log.error("starter socketHandler is null");
            return false;
        }
        socketHandler.setDispatch(dispatcher);
        socketHandler.setTrigger(trigger);
        return true;
    }

    @Override
    public boolean initInitializer() {
        if (initializer == null) {
            log.error("starter initializer is null");
            return false;
        }
        initializer.setHandler(socketHandler);

        return true;
    }

    @Override
    public boolean initServerOrClient() {
        if (nettyClient == null) {
            log.error("starter nettyClient is null");
            return false;
        }
        nettyClient.setInitializer(initializer);
        return true;
    }

    public boolean start() {
        if (!initAllComponent()) {
            log.error("init component have error");
            return false;
        }

        List<ServerCfgPojo> gatewayList = this.parseGateway();
        if(gatewayList == null || gatewayList.isEmpty()){
            return false;
        }
        for (ServerCfgPojo gateway:gatewayList) {
            nettyClient.start(gateway.getIp(), gateway.getPort());
            log.info("clint connection gateway:{} suss",gateway);
        }
        return true;

    }


    private List<ServerCfgPojo> parseGateway() {
        if (StrUtil.isEmpty(this.gateways)) {
            log.error("{}, gateway config is null or empty.",serverConfig);
            return null;
        }
        String[] gatewayIpAndPortArr = this.gateways.split(StringConstant.SEMICOLON);
        if (gatewayIpAndPortArr.length == 0) {
            return null;
        }

        List<ServerCfgPojo> gatewayServerList = new ArrayList<>(gatewayIpAndPortArr.length);
        for (String ipAndPortStr : gatewayIpAndPortArr) {
            String[] ipAndPortArr = ipAndPortStr.split(StringConstant.COLON);
            if(ipAndPortArr.length != 2){
                log.error("{}, gateway config have error.cfg:{}",serverConfig,this.gateways);
                return null;
            }
            String ip = ipAndPortArr[0];
            Integer port;
            try{
                port = Integer.valueOf(ipAndPortArr[1]);
            }catch (NumberFormatException exception){
                log.error("{}, gateway config port error.cfg:{}",serverConfig,this.gateways);
                return null;
            }

            ServerCfgPojo gateway = new ServerCfgPojo(ip,port);
            gatewayServerList.add(gateway);
        }
        return gatewayServerList;

    }
}
