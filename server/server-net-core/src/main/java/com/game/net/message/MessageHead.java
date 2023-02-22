package com.game.net.message;

import io.netty.util.internal.StringUtil;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2022/12/29
 * <p>
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

@Data
@Slf4j
@ToString
public class MessageHead {
    private short msgId;
    private short code;
    private long timestamp;
    private int seq;
    private String sign;
    private short length;

    protected boolean writeCheck() {
        if (this.msgId < 0) {
            log.error("error:send message head msgId:{}", msgId);
            return false;
        }

        if (this.timestamp <= 0) {
            log.error("error:send message head timestamp:{}", timestamp);
            return false;
        }

        if (this.seq < 0) {
            log.error("error:send message head seq:{}", seq);
            return false;
        }

        if (StringUtil.isNullOrEmpty(sign)) {
            log.error("error:send message head sign:{}", sign);
            return false;
        }

        return true;
    }



}
