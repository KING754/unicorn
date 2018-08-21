package org.unicorn.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty server的启动入口类
 * 
 * @author KING
 * @date 2018年8月21日
 */

public class NettyServer {
    private static final Logger LOGGER      = LoggerFactory.getLogger(NettyServer.class);
    private EventLoopGroup      bossGroup   = null;
    private EventLoopGroup      workerGroup = null;
    private ServerBootstrap     bootStrap   = null;
    
    public void  launch(int serverPort){
        String osName = System.getProperty("os.name");
        this.initThreadGroup(osName);
        
        bootStrap = new ServerBootstrap();
        bootStrap.group(bossGroup, workerGroup);
        this.initServerChannel(osName);
        
        bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootStrap.childOption(ChannelOption.SO_REUSEADDR, true);
        
        bootStrap.childHandler(new ServerChannel());
        ChannelFuture future = bootStrap.bind(serverPort);
        future.addListener(new ChannelFutureListener() {
            
            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (arg0.isSuccess()) {
                    LOGGER.info("Netty服务启动成功！...监听端口：【{}】", 12000);
                } else {
                    LOGGER.info("Netty服务启动失败！...端口【{}】可能被占用！进程自动结束！", 12000);
                    System.exit(0);
                }
            }
        });
    }
    
    private void initThreadGroup(String osName){
        if(osName.trim().equals("Linux")){
            this.bossGroup = new EpollEventLoopGroup(ServerCoreInfo.DEFAULT_NETTY_CORE_THREAD_NUM);
            this.workerGroup = new EpollEventLoopGroup(ServerCoreInfo.DEFAULT_NETTY_WORK_THREAD_NUM);
        }else{
            this.bossGroup = new NioEventLoopGroup(ServerCoreInfo.DEFAULT_NETTY_CORE_THREAD_NUM);
            this.workerGroup = new NioEventLoopGroup(ServerCoreInfo.DEFAULT_NETTY_WORK_THREAD_NUM);
        }
    }
    
    private void initServerChannel(String osName){
        if(osName.trim().equals("Linux")){
            this.bootStrap.channel(EpollServerSocketChannel.class);
        }else{
            this.bootStrap.channel(NioServerSocketChannel.class);
        }
    }

}
