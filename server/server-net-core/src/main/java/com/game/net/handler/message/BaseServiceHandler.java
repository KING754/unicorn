package com.game.net.handler.message;

import com.game.constant.NettyConstant;
import com.game.constant.ServerConstant;
import com.game.net.message.InnerMessage;
import com.game.net.message.Message;
import com.game.net.message.MessageHead;
import com.game.rpc.message.inner.InnerRPC;
import com.game.rpc.message.st.Code;
import com.game.rpc.message.st.MsgId;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author KING
 * @date 2023/02/16
 */
@Slf4j
public class BaseServiceHandler {
    public void sendMessage(ChannelHandlerContext ctx, Message msgObj) {
        ctx.writeAndFlush(msgObj);
    }

    public void sendMessage(ChannelHandlerContext ctx, MsgId.EN_MessageId msgId, byte[] bodyBytes, int reqSeq, Code.En_Code code) {
        Message message = this.buildMessage(msgId, bodyBytes, reqSeq, code);
        this.sendMessage(ctx, message);
    }

    public void sendErrorMessage(ChannelHandlerContext ctx, MsgId.EN_MessageId msgId, int reqSeq, Code.En_Code code) {
        Message message = this.buildMessage(msgId, null, reqSeq, code);
        this.sendMessage(ctx, message);
    }

    public void sendInnerMessage(ChannelHandlerContext ctx, InnerRPC.EN_INNER_CMD_ID cmdId, InnerRPC.EN_INNER_CODE innerCode, byte[] bodyBytes) {
        InnerMessage innerMessage = this.buildInnerMessage(cmdId, innerCode, null, MsgId.EN_MessageId.E_ERROR_ID, bodyBytes, 0, Code.En_Code.E_SUSS);
        this.sendMessage(ctx, innerMessage);
    }

    public void sendInnerErrorMessage(ChannelHandlerContext ctx, InnerRPC.EN_INNER_CMD_ID cmdId, InnerRPC.EN_INNER_CODE innerCode) {
        InnerMessage innerMessage = this.buildInnerMessage(cmdId, innerCode, null, MsgId.EN_MessageId.E_ERROR_ID, null, 0, Code.En_Code.E_SUSS);
        this.sendMessage(ctx, innerMessage);
    }


    public void sendNeedForwardMessage(ChannelHandlerContext ctx, String playerId, MsgId.EN_MessageId msgId, byte[] bodyBytes, int reqSeq, Code.En_Code code) {
        InnerMessage innerMessage = this.buildInnerMessage(InnerRPC.EN_INNER_CMD_ID.E_FORWARD_MSG, InnerRPC.EN_INNER_CODE.E_INNER_SUSS, playerId, msgId, bodyBytes, reqSeq, code);
        this.sendMessage(ctx, innerMessage);
    }


    public void sendNeedForwardMessage(ChannelHandlerContext ctx, String playerId, Message message) {
        InnerMessage innerMessage = this.buildInnerMessage(InnerRPC.EN_INNER_CMD_ID.E_FORWARD_MSG, InnerRPC.EN_INNER_CODE.E_INNER_SUSS, playerId,message);
        this.sendMessage(ctx, innerMessage);
    }


    public void sendNeedForwardErrorMessage(ChannelHandlerContext ctx, String playerId, MsgId.EN_MessageId msgId, int reqSeq, Code.En_Code code) {
        InnerMessage innerMessage = this.buildInnerMessage(InnerRPC.EN_INNER_CMD_ID.E_FORWARD_MSG, InnerRPC.EN_INNER_CODE.E_INNER_SUSS, playerId, msgId, null, reqSeq, code);
        this.sendMessage(ctx, innerMessage);
    }


    public String signMessage(Message message) {
        ByteBuffer buffer = ByteBuffer.allocate(NettyConstant.SIGN_HEAD_LEN + message.getBodyLength());
        MessageHead head = message.getHead();
        buffer.putShort(head.getMsgId());
        buffer.putShort(head.getCode());
        buffer.putLong(head.getTimestamp());
        buffer.putInt(head.getSeq());
        if (message.getBody() != null) {
            buffer.put(message.getBody());
        }

        byte[] signBody = buffer.array();
        return this.signBytes(signBody);
    }

    private String signBytes(byte[] signBytes) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(signBytes);
            StringBuffer hexValue = new StringBuffer();
            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private InnerMessage buildInnerMessage(InnerRPC.EN_INNER_CMD_ID cmdId, InnerRPC.EN_INNER_CODE innerCode, String playId,
                                           MsgId.EN_MessageId msgId, byte[] bodyBytes, int reqSeq, Code.En_Code code) {
        InnerMessage innerMessage = this.buildInnerMsgOuter(cmdId,innerCode,playId);

        Message message = null;
        int len  = NettyConstant.INNER_MSG_HEAD_LEN;
        if (msgId != null) {
            message = this.buildMessage(msgId, bodyBytes, reqSeq, code);
            innerMessage.setHead(message.getHead());
            innerMessage.setBody(message.getBody());

            len += message.getHead().getLength();
        }

        innerMessage.setLen((short)(len));
        return innerMessage;
    }

    private InnerMessage buildInnerMessage(InnerRPC.EN_INNER_CMD_ID cmdId, InnerRPC.EN_INNER_CODE innerCode, String playId,Message message){
        InnerMessage innerMessage = this.buildInnerMsgOuter(cmdId,innerCode,playId);
        int msgLen = NettyConstant.INNER_MSG_HEAD_LEN;
        if(message != null){
            innerMessage.setHead(message.getHead());
            innerMessage.setBody(message.getBody());
            msgLen += message.getHead().getLength();
        }

        innerMessage.setLen((short)(msgLen));
        return innerMessage;
    }

    private InnerMessage buildInnerMsgOuter(InnerRPC.EN_INNER_CMD_ID cmdId, InnerRPC.EN_INNER_CODE innerCode, String playId){
        InnerMessage innerMessage = new InnerMessage();
        innerMessage.setServerMsgId((short) cmdId.getNumber());
        innerMessage.setCode((short) innerCode.getNumber());
        if (StringUtil.isNullOrEmpty(playId)) {
            innerMessage.setPlayerId(ServerConstant.MESSAGE_DEFAULT_PLAYER_ID);
        } else {
            innerMessage.setPlayerId(playId);
        }
        return innerMessage;
    }

    private Message buildMessage(MsgId.EN_MessageId msgId, byte[] bodyBytes, int reqSeq, Code.En_Code code) {
        if (msgId == null) {
            return null;
        }

        int bodyLen = 0;
        if (bodyBytes != null) {
            bodyLen = bodyBytes.length;
        }
        short totalLength = (short) (NettyConstant.MSG_HEAD_LEN + bodyLen);
        MessageHead head = new MessageHead();
        head.setLength(totalLength);

        head.setMsgId((short) msgId.getNumber());
        head.setCode((short) code.getNumber());
        head.setTimestamp(System.currentTimeMillis());
        head.setSeq(reqSeq);


        Message message = new Message();
        message.setHead(head);
        if (bodyBytes != null) {
            message.setBody(bodyBytes);
        }

        String sign = this.signMessage(message);
        head.setSign(sign);
        return message;
    }
}
