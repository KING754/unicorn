package com.game.net.coder;

import com.game.constant.NettyConstant;
import com.game.net.message.InnerMessage;
import com.game.net.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

/**
 * @author KING
 * @date 2022/12/29
 * ## Message 规则
 * |-----------------------------------------------------------------------------|
 * |                 HEAD        |             BODY                              |
 * |-----------------------------------------------------------------------------|
 * |LEN|SIGN|MSG-ID|timestamp|seq|             BODY                              |
 * |-----------------------------------------------------------------------------|
 * |2  |32  |2     |8        |4  |          32767-(2+32+2+8+4)                   |
 * |-----------------------------------------------------------------------------|
 * <p>
 * <p>
 * - HEAD 消息头
 * - BODY 消息体
 * <p>
 * - MSG-ID
 * 消息ID 2字节.0x1~0xFFFF,请求与返回为不一样的ID.
 * - timestamp
 * 时间戳，8个字节.
 * - seq
 * 序列号,4个字节.客户端维护.每个用户从登录连接开始递增.
 * - sign
 * 签名：MD5(MSG-ID&timestamp&seq&body)
 * - LEN 整个包总长度
 * 2+8+4+32+2+BODY.length.目前最大度32767
 */
@Slf4j
public class InnerMessageDecoder extends ClientMessageDecoder {


    public InnerMessageDecoder() {
        super();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx,in);
    }

    public InnerMessage decodeToMessage(ByteBuf byteBuffer) throws UnsupportedEncodingException {
        if (null == byteBuffer) {
            return null;
        }

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        int readAbleLen = byteBuffer.readableBytes();
        if (readAbleLen < NettyConstant.INNER_MSG_HEAD_LEN) {
            log.warn("inner decode error less than head length! readAbleLen:{}",readAbleLen);
            return null;
        }

        short totalLen = byteBuffer.readShortLE();
        if (readAbleLen < totalLen) {
            log.warn("inner decode error total length error! readAbleLen:{},totalLen:{}",readAbleLen,totalLen);
            return null;
        }

        InnerMessage innerMessage = new InnerMessage();

        innerMessage.setLen(totalLen);
        innerMessage.setServerMsgId(byteBuffer.readShortLE());
        innerMessage.setCode(byteBuffer.readShortLE());

        byte[] playerIdBytes = new byte[NettyConstant.MSG_PLAYER_ID];
        byteBuffer.readBytes(playerIdBytes);
        innerMessage.setPlayerId(new String(playerIdBytes, NettyConstant.DEFAULT_STRING_CHARSET_NAME));

        int bodyLength = totalLen - NettyConstant.INNER_MSG_HEAD_LEN;
        if(bodyLength == 0){
            return innerMessage;
        }

        Message clientMsg = super.decodeToMessage(byteBuffer);
        if(clientMsg != null){
            innerMessage.setHead(clientMsg.getHead());
            innerMessage.setBody(clientMsg.getBody());
        }
        return innerMessage;
    }
}
