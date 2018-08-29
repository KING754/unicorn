package org.unicorn.common.netty;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏协议包类，描述具体游戏数据包结构。<br>
 * <br>
 * 封包规则：一个包分为包头和包体两部分，结构如下：<br>
 * 消息长度（占用2字节）+消息TYPE( 占用4字节)+消息体（N字节）
 * @author KING
 * @date 2018年8月29日
 */

public class CommonMessage implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonMessage.class);
    private static final long serialVersionUID = 1L;
    
    private static final int HEAD_LEN_BYTE_LEN = 2;
    private static final int HEAD_MSG_ID_BYTE_LEN = 4;
    private static final int HEAD_TOTAL_LEN = HEAD_LEN_BYTE_LEN+HEAD_MSG_ID_BYTE_LEN;
    
    

}
