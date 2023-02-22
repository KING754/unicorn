package com.game.net.coder;

import com.game.constant.NettyConstant;
import com.game.net.message.Message;
import com.game.net.message.MessageHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
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
public class ClientMessageDecoder extends LengthFieldBasedFrameDecoder {


    public ClientMessageDecoder() {
        super(ByteOrder.LITTLE_ENDIAN,NettyConstant.MAX_MSG_LEN, 0, NettyConstant.LENGTH_FIELD_LEN,
                -(NettyConstant.LENGTH_FIELD_LEN),
                0,true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            return this.decodeToMessage(frame);
        } catch (Exception e) {
            log.error("decode exception.{}",e);
        }
        return null;
    }

    public Message decodeToMessage(ByteBuf byteBuffer) throws UnsupportedEncodingException {
        if (null == byteBuffer) {
            return null;
        }

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        int readAbleLen = byteBuffer.readableBytes();
        if (readAbleLen < NettyConstant.MSG_HEAD_LEN) {
            log.warn("decode error less than head length! readAbleLen:{}",readAbleLen);
            return null;
        }

        short totalLen = byteBuffer.readShortLE();
        if (readAbleLen < totalLen) {
            log.warn("decode error total length error! readAbleLen:{},totalLen:{}",readAbleLen,totalLen);
            return null;
        }

        MessageHead head = new MessageHead();

        head.setLength(totalLen);

        byte[] signBytes = new byte[NettyConstant.SIGN_LEN];
        byteBuffer.readBytes(signBytes);
        head.setSign(new String(signBytes, NettyConstant.DEFAULT_STRING_CHARSET_NAME));

        head.setMsgId(byteBuffer.readShortLE());
        head.setCode(byteBuffer.readShortLE());
        head.setTimestamp(byteBuffer.readLongLE());
        head.setSeq(byteBuffer.readIntLE());

        Message msg = new Message();
        msg.setHead(head);

        int bodyLength = totalLen - NettyConstant.MSG_HEAD_LEN;
        byte[] bodyBytes = null;
        if (bodyLength > 0) {
            bodyBytes = new byte[bodyLength];
            byteBuffer.readBytes(bodyBytes);
        }
        msg.setBody(bodyBytes);
        return msg;
    }

    //    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf inputByteBuf, List<Object> list) throws Exception {
//        int readAbleLen = inputByteBuf.readableBytes();
//        if (readAbleLen < NettyConstant.MSG_HEAD_LEN) {
//            return;
//        }
//        short totalLen = inputByteBuf.readShort();
//        if (readAbleLen < totalLen) {
//            //此处read了，是不是需要重置位置.
//            return;
//        }
//        MessageHead head = new MessageHead();
//        head.setLength(totalLen);
//        head.setMsgId(inputByteBuf.readShort());
//        head.setTimestamp(inputByteBuf.readLong());
//        head.setSeq(inputByteBuf.readInt());
//
//        byte[] signBytes = new byte[NettyConstant.SIGN_LEN];
//        inputByteBuf.readBytes(signBytes);
//        head.setSign(new String(signBytes, NettyConstant.DEFAULT_STRING_CHARSET_NAME));
//
//        Message msg = new Message();
//        msg.setHead(head);
//
//        int bodyLength = totalLen - NettyConstant.MSG_HEAD_LEN;
//        byte[] bodyBytes = new byte[totalLen - bodyLength];
//        inputByteBuf.readBytes(bodyBytes);
//
//        msg.setBody(bodyBytes);
//    }
}
