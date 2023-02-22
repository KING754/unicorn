package com.game.net.message;

import com.game.constant.NettyConstant;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KING
 * @date 2023/02/10
 */
@Slf4j
@Data
@ToString
public class InnerMessage extends Message{
    private short len;
    private short serverMsgId;
    private short code;
    private String playerId;

    @Override
    public boolean writeCheck() {
        if(len <= 0){
            log.error("inner msg len less than 0.len={}",len);
            return false;
        }

        if(serverMsgId <= 0){
            log.error("inner msg serverMsgId less than 0.serverMsgId={}",serverMsgId);
            return false;
        }

        int messageLen = 0;
        if(this.getHead() != null){
            messageLen = this.getHead().getLength();
        }
        if(this.getLen() != messageLen+NettyConstant.INNER_MSG_HEAD_LEN){
            log.error("inner msg len have error.len={},msgId={},PID={},messageLen={}",len,serverMsgId,playerId,messageLen);
            return false;
        }
        if(this.getHead() != null){
            return super.writeCheck();
        }else {
            return true;
        }
    }
}
