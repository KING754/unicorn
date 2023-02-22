package com.game.client.netty;

import com.game.net.dispatcher.BaseDispatcher;
import com.game.net.handler.message.OuterClientHandle;
import com.game.net.handler.message.manager.OuterClientHandlerManager;
import com.game.net.message.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Scanner;


/**
 * @author KING
 * @date 2022/12/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TestClientDispatcher extends BaseDispatcher {
    private final OuterClientHandlerManager clientHandlerManager;

    public void dispatch(ChannelHandlerContext ctx, Message message){
        if (message != null && message.getHead() != null) {
            short msgId = message.getHead().getMsgId();
            if (msgId > 0) {
                OuterClientHandle handler = clientHandlerManager.getHandlerByMsgId(msgId);
                if (handler != null && handler instanceof OuterClientHandle) {
                    try {
                        handler.clientExecute(ctx, message.getHead().getCode(),message.getBody(), message.getHead().getSeq());
                    } catch (Exception e) {
                        //TODO 根据异常发送一个消息
                        throw new RuntimeException(e);
                    }
                } else {
                    log.error("dispatch fail.client not handle for:{}", msgId);
                }
            }
        }
        short choose = this.chooseTestHandle(ctx);
        OuterClientHandle handler = clientHandlerManager.getHandlerByMsgId(choose);
        if(handler instanceof OuterClientHandle && handler != null){
            try {
                ((OuterClientHandle)handler).sendTestData(ctx);
            } catch (UnsupportedEncodingException e) {
               log.error("client first send msg error.",e);
            }
        }
    }


    private short chooseTestHandle(ChannelHandlerContext ctx) {
        System.out.println("请选择想要测试的功能");
        for (Map.Entry<Short, OuterClientHandle> handle: clientHandlerManager.getHandlerMap().entrySet()) {
            System.out.println(handle.getKey()+"  :   "+handle.getValue().getClass().getSimpleName());
        }
        Scanner scanner = new Scanner(System.in);
        return scanner.nextShort();
    }

    private void sendTestData(ChannelHandlerContext ctx, short msgId) throws UnsupportedEncodingException {
        OuterClientHandle handler = clientHandlerManager.getHandlerByMsgId(msgId);
        if (handler instanceof OuterClientHandle && handler != null) {
            handler.sendTestData(ctx);
        } else {
            log.error("send test data fail.client not handle for:{}", msgId);
        }
    }
}
