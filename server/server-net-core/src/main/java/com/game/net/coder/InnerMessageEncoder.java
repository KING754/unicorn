package com.game.net.coder;

import com.game.constant.NettyConstant;
import com.game.net.message.InnerMessage;
import com.game.net.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2022/12/29
 * <p>
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
public class InnerMessageEncoder extends ClientMessageEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        if (message == null) {
            log.info("inner send message is null.");
            return;
        }
        if(!(message instanceof InnerMessage)){
            log.info("inner send message not is InnerMessage.msg={}",message);
            return;
        }
        InnerMessage innerMessage = (InnerMessage)message;

        if (!innerMessage.writeCheck()) {
            return;
        }

        byteBuf.writeShortLE(innerMessage.getLen());
        byteBuf.writeShortLE(innerMessage.getServerMsgId());
        byteBuf.writeShortLE(innerMessage.getCode());

        byte[] playerIdBytes = innerMessage.getPlayerId().getBytes(NettyConstant.DEFAULT_STRING_CHARSET_NAME);
        byteBuf.writeBytes(playerIdBytes);

        super.encode(channelHandlerContext,message,byteBuf);
    }
}
