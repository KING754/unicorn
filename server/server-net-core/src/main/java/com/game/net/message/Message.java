package com.game.net.message;

import com.game.constant.NettyConstant;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2022/12/29
 * ## Message 规则
 * |-----------------------------------------------------------------------------|
 * |                 HEAD        |             BODY                              |
 * |-----------------------------------------------------------------------------|
 * |LEN|MSG-ID|timestamp|seq|SIGN|             BODY                              |
 * |-----------------------------------------------------------------------------|
 * |2  |2     |8        |4  |32  |          32767-(2+8+4+32+2)                   |
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
@Data
@ToString
public class Message {
    private MessageHead head;
    private byte[] body;

    public boolean writeCheck() {
        if (this.head == null) {
            log.error("send msg head is null");
            return false;
        }
        int bodyLen = 0;
        if(body != null){
            bodyLen = body.length;
        }

        int totalLen = NettyConstant.MSG_HEAD_LEN + bodyLen;
        if (this.head.getLength() != totalLen) {
            log.error("send msg length is error.head len:{},fact:{}", this.head.getLength(), totalLen);
            return false;
        }

        if (!this.head.writeCheck()) {
            return false;
        }
        return true;
    }

    public int getBodyLength(){
        if(this.body == null){
            return 0;
        }
        return this.body.length;
    }

}
