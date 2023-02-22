package com.game.exception.inner;

import com.game.exception.LogicException;
import com.game.rpc.message.inner.InnerRPC;

/**
* @author KING
* @date 2023/2/15 18:42:43
*/
public class InnerUnknownException extends LogicException {
    public InnerUnknownException(String message) {
        super(InnerRPC.EN_INNER_CODE.E_INNER_UNKNOWN_ERROR,null, message);
    }
}
