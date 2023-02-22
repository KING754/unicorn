package com.game.exception;

import com.game.rpc.message.inner.InnerRPC;
import com.game.rpc.message.st.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author KING
 * @date 2023/01/30
 */
@Data
@ToString
@AllArgsConstructor
public class LogicException extends RuntimeException{
    private InnerRPC.EN_INNER_CODE innerCode;
    private Code.En_Code code;
    private String message;
}
