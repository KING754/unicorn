package com.game.client.handler;

import com.game.client.constant.ClientConstant;
import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.OuterClientHandle;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
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
@HandlerTrigger(msgId = MsgId.EN_MessageId.E_ROOM_BATTLE_REPORT_VALUE)
public class RoomBattleReportHandler extends OuterClientHandle {
    @Override
    public void clientExecute(ChannelHandlerContext ctx, short code, byte[] bodyBytes, int reqSeq) {
        if(code != Code.En_Code.E_SUSS_VALUE){
            log.info("receive E_ROOM_BATTLE_REPORT code:{}", code);
            return;
        }
        if(bodyBytes == null || bodyBytes.length <= 0){
            return;
        }
        log.info("receive E_ROOM_BATTLE_REPORT code：{}", code);
    }

    @Override
    public void sendTestData(ChannelHandlerContext ctx) {
        super.sendMessage(ctx, MsgId.EN_MessageId.E_ROOM_BATTLE_REPORT, null, ClientConstant.atomicInteger.getAndIncrement(), Code.En_Code.E_SUSS);
    }
}