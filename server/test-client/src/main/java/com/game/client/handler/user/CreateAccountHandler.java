package com.game.client.handler.user;

import com.game.client.constant.ClientConstant;
import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.OuterClientHandle;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
import com.game.rpc.message.st.user.LoginStructs;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author KING
 * @date 2023/1/31 11:15:39
 */
@Slf4j
@Service
@RequiredArgsConstructor
@HandlerTrigger(msgId = MsgId.EN_MessageId.E_CREATE_ACCOUNT_VALUE)
public class CreateAccountHandler extends OuterClientHandle {
    @Override
    public void clientExecute(ChannelHandlerContext ctx, short code, byte[] bodyBytes, int reqSeq) {
        if(code != Code.En_Code.E_SUSS_VALUE){
            log.info("receive E_CREATE_ACCOUNT code:{}", code);
            return;
        }
        if(bodyBytes == null || bodyBytes.length <= 0){
            return;
        }
        log.info("receive E_CREATE_ACCOUNT code：{}", code);
    }

    @Override
    public void sendTestData(ChannelHandlerContext ctx) {
        LoginStructs.CreateAccountReq.Builder req = LoginStructs.CreateAccountReq.newBuilder();
        req.setAccountName("KING754");
        req.setPassword("123456");
        super.sendMessage(ctx, MsgId.EN_MessageId.E_CREATE_ACCOUNT, req.build().toByteArray(), ClientConstant.atomicInteger.getAndIncrement(), Code.En_Code.E_SUSS);
    }


}