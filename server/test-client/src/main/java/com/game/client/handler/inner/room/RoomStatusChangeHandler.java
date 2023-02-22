package com.game.client.handler.inner.room;

import com.game.client.constant.ClientConstant;
import com.game.exception.logic.ReqParseException;
import com.game.net.annotation.HandlerTrigger;
import com.game.net.handler.message.OuterClientHandle;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
import com.game.rpc.message.st.inner.room.Room;
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
@HandlerTrigger(msgId = MsgId.EN_MessageId.E_ROOM_STATUS_CHANGE_VALUE)
public class RoomStatusChangeHandler extends OuterClientHandle {
    @Override
    public void clientExecute(ChannelHandlerContext ctx, short code, byte[] bodyBytes, int reqSeq) {
        if(code != Code.En_Code.E_SUSS_VALUE){
            log.info("receive E_ROOM_STATUS_CHANGE code:{}", code);
            return;
        }
        if(bodyBytes == null || bodyBytes.length <= 0){
            return;
        }
        Room.RoomStatusChangeResp resp = null;
        try{
            resp = Room.RoomStatusChangeResp.parseFrom(bodyBytes);
        }catch (Exception e) {
            log.error("RoomStatusChangeHandler RoomStatusChangeResp parse req have error.",e);
            throw new ReqParseException("RoomStatusChangeHandler RoomStatusChangeResp parse error.");
        }

        log.info("receive E_ROOM_STATUS_CHANGE code:{} RoomStatusChangeResp：{}", code, resp);
    }

    @Override
    public void sendTestData(ChannelHandlerContext ctx) {
        Room.RoomStatusChangeReq.Builder req = Room.RoomStatusChangeReq.newBuilder();
        super.sendMessage(ctx, MsgId.EN_MessageId.E_ROOM_STATUS_CHANGE, req.build().toByteArray(), ClientConstant.atomicInteger.getAndIncrement(), Code.En_Code.E_SUSS);
    }
}